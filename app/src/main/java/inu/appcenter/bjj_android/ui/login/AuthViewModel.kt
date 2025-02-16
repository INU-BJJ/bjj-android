package inu.appcenter.bjj_android.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
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
    val signupEmail: String = "",
    val socialName: String = "",
    val saveTokenState: Boolean? = null,
    val hasToken: Boolean? = null
)

class AuthViewModel(
    private val memberRepository: MemberRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

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
                    _uiState.update { it.copy(hasToken = hasToken) }
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
            }
        }
    }

    fun signup(signupReq: SignupReq) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(signupState = AuthState.Loading) }

                val response = memberRepository.signup(signupReq)
                if (response.isSuccessful) {
                    val tokenResponse = response.body() ?: throw Exception("회원가입 정보가 비어있습니다.")
                    dataStoreManager.saveToken(tokenResponse.token)
                    _uiState.update { it.copy(
                        signupState = AuthState.Success,
                        socialName = ""
                    )}
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("signup", e.message.toString())
                _uiState.update { it.copy(
                    signupState = AuthState.Error(e.message ?: UNKNOWN_ERROR)
                )}
            } catch (e: RuntimeException) {
                Log.e("signup timeout", e.message.toString())
                _uiState.update { it.copy(
                    signupState = AuthState.Error(TIMEOUT_ERROR)
                )}
            }
        }
    }

    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(checkNicknameState = AuthState.Loading) }

                val response = memberRepository.checkNickname(nickname)
                if (response.isSuccessful) {
                    val isAvailable = response.body() ?: throw Exception("닉네임 검사 정보가 비어있습니다.")
                    _uiState.update { it.copy(
                        checkNicknameState = if (isAvailable) AuthState.Success
                        else AuthState.Error("중복")
                    )}
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("checkNickname", e.message.toString())
                _uiState.update { it.copy(
                    checkNicknameState = AuthState.Error(e.message ?: UNKNOWN_ERROR)
                )}
            } catch (e: RuntimeException) {
                Log.e("checkNickname timeout", e.message.toString())
                _uiState.update { it.copy(
                    checkNicknameState = AuthState.Error(TIMEOUT_ERROR)
                )}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(logoutState = AuthState.Loading) }
            try {
                dataStoreManager.clearToken()
                getKoin().getAll<ViewModel>().forEach {
                    when (it) {
                        is MainViewModel -> it.resetState()
                        is MenuDetailViewModel -> it.resetState()
                        // 다른 ViewModel들도 필요에 따라 추가
                    }
                }
                _uiState.update { it.copy(
                    logoutState = AuthState.Success,
                    hasToken = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    logoutState = AuthState.Error(e.message ?: "Unknown error")
                )}
            }
        }
    }

    fun resetNicknameCheckState() {
        _uiState.update { it.copy(
            checkNicknameState = AuthState.Idle
        )}
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
}