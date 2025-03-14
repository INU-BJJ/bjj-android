package inu.appcenter.bjj_android.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.fcm.FcmManager
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

private const val UNKNOWN_ERROR = "Unknown error"
private const val TIMEOUT_ERROR = "Signup timed out"

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

data class AuthUiState(
    val signupState: AuthState = AuthState.Idle,
    val checkNicknameState: AuthState = AuthState.Idle,
    val logoutState: AuthState = AuthState.Idle,
    val deleteAccountState: AuthState = AuthState.Idle,
    val signupEmail: String = "",
    val socialName: String = "",
    val saveTokenState: Boolean? = null,
    val hasToken: Boolean? = null
)

class AuthViewModel(
    private val memberRepository: MemberRepository,
    private val dataStoreManager: DataStoreManager,
    private val fcmManager: FcmManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeToken()
    }

    private fun observeToken() {
        viewModelScope.launch {
            dataStoreManager.token
                .map { !it.isNullOrEmpty() }
                .collect { hasToken ->
                    val previousState = _uiState.value.hasToken
                    _uiState.update { it.copy(hasToken = hasToken) }

                    // 토큰 상태가 변경될 때만 FCM 처리 (이전 상태와 현재 상태가 다를 때)
                    if (hasToken && hasToken != previousState) {
                        // 사용자가 로그인했을 때 FCM 토큰 등록
                        fcmManager.onUserLogin()
                    }
                }
        }
    }

    fun setSignupEmail(email: String) {
        _uiState.update { it.copy(signupEmail = email) }
    }

    fun setSocialName(social: String) {
        _uiState.update { it.copy(socialName = social) }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            try {
                dataStoreManager.saveToken(token)
                _uiState.update { it.copy(saveTokenState = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(saveTokenState = false) }
                emitError(AppError.UnknownError(e.message ?: "토큰 저장 중 오류가 발생했습니다."))
            }
        }
    }

    fun signup(signupReq: SignupReq) {
        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(signupState = AuthState.Loading) }

            memberRepository.signup(signupReq).handleResponse(
                onSuccess = { tokenResponse ->
                    dataStoreManager.saveToken(tokenResponse.token)
                    _uiState.update {
                        it.copy(
                            signupState = AuthState.Success,
                            socialName = ""
                        )
                    }

                    // 회원가입 성공 후 FCM 토큰 등록
                    fcmManager.onUserLogin()
                },
                onError = { error ->
                    Log.e("signup", error.message ?: "Unknown error")
                    _uiState.update {
                        it.copy(
                            signupState = AuthState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )
        }
    }

    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(checkNicknameState = AuthState.Loading) }

            memberRepository.checkNickname(nickname).handleResponse(
                onSuccess = { isAvailable ->
                    _uiState.update {
                        it.copy(
                            checkNicknameState = if (isAvailable) AuthState.Success
                            else AuthState.Error("중복")
                        )
                    }
                },
                onError = { error ->
                    Log.e("checkNickname", error.message ?: "Unknown error")
                    _uiState.update {
                        it.copy(
                            checkNicknameState = AuthState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(logoutState = AuthState.Loading) }

            try {
                dataStoreManager.clearToken()

                // 다른 ViewModel 상태 초기화
                getKoin().getAll<androidx.lifecycle.ViewModel>().forEach {
                    when (it) {
                        is MainViewModel -> it.resetState()
                        is MenuDetailViewModel -> it.resetState()
                        // 다른 ViewModel들도 필요에 따라 추가
                    }
                }

                _uiState.update {
                    it.copy(
                        logoutState = AuthState.Success,
                        hasToken = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        logoutState = AuthState.Error(e.message ?: "Unknown error")
                    )
                }
                emitError(AppError.UnknownError(e.message ?: "로그아웃 중 오류가 발생했습니다."))
            } finally {
                setLoading(false)
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(deleteAccountState = AuthState.Loading) }

            memberRepository.deleteAccount().handleResponse(
                onSuccess = {
                    // 토큰 삭제 및 상태 초기화
                    dataStoreManager.clearToken()

                    // 다른 ViewModel 상태 초기화
                    getKoin().getAll<androidx.lifecycle.ViewModel>().forEach {
                        when (it) {
                            is MainViewModel -> it.resetState()
                            is MenuDetailViewModel -> it.resetState()
                            // 다른 ViewModel들도 필요에 따라 추가
                        }
                    }

                    _uiState.update {
                        it.copy(
                            deleteAccountState = AuthState.Success,
                            hasToken = false
                        )
                    }
                },
                onError = { error ->
                    Log.e("deleteAccount", error.message ?: "Unknown error")
                    _uiState.update {
                        it.copy(
                            deleteAccountState = AuthState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )
        }
    }

    fun resetNicknameCheckState() {
        _uiState.update {
            it.copy(
                checkNicknameState = AuthState.Idle
            )
        }
    }

    fun resetState() {
        _uiState.update {
            it.copy(
                signupState = AuthState.Idle,
                checkNicknameState = AuthState.Idle,
                logoutState = AuthState.Idle,
                saveTokenState = null
            )
        }
    }

    fun resetDeleteAccountState() {
        _uiState.update { it.copy(deleteAccountState = AuthState.Idle) }
    }
}