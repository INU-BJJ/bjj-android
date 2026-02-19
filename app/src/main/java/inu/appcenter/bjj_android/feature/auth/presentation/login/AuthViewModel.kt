package inu.appcenter.bjj_android.feature.auth.presentation.login

import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.core.notification.FcmManager
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.feature.auth.data.MemberRepository
import inu.appcenter.bjj_android.feature.main.presentation.MainViewModel
import inu.appcenter.bjj_android.feature.menudetail.presentation.MenuDetailViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.MyPageViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.feature.ranking.presentation.RankingViewModel
import inu.appcenter.bjj_android.feature.review.presentation.ReviewViewModel
import inu.appcenter.bjj_android.core.util.AppError
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import inu.appcenter.bjj_android.model.member.LoginReq
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

sealed class WebViewEvent {
    data class PageStarted(val url: String) : WebViewEvent()
    data class PageFinished(val url: String) : WebViewEvent()
    data class LoginSuccess(val token: String) : WebViewEvent()
    data class SignupSuccess(val email: String) : WebViewEvent()
    data class Error(val message: String) : WebViewEvent()
    object RenderProcessGone : WebViewEvent()
}

data class AuthUiState(
    val signupState: AuthState = AuthState.Idle,
    val checkNicknameState: AuthState = AuthState.Idle,
    val logoutState: AuthState = AuthState.Idle,
    val deleteAccountState: AuthState = AuthState.Idle,
    val signupEmail: String = "",
    val socialName: String = "",
    val socialProviderId: String = "",
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

    private val _webViewEvents = MutableSharedFlow<WebViewEvent>()
    val webViewEvents = _webViewEvents.asSharedFlow()

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

    fun setSocialProviderId(providerId: String) {
        _uiState.update { it.copy(socialProviderId =
            providerId) }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            try {
                dataStoreManager.saveToken(token)
                _uiState.update { it.copy(saveTokenState = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(saveTokenState = false) }
                emitError(AppError.NotFoundError(e.message ?: "토큰 저장 중 오류가 발생했습니다."))
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

    fun login(providerId: String, provider: String) {
        viewModelScope.launch {
            setLoading(true)
            memberRepository.login(
                LoginReq(
                    providerId =
                        providerId, provider = provider
                )
            ).handleResponse(
                onSuccess = { tokenResponse ->

                    dataStoreManager.saveToken(tokenResponse.token)
                    fcmManager.onUserLogin()
                    _uiState.update { it.copy(signupState =
                        AuthState.Success) }
                },
                onError = { _ ->
                    // 신규 회원 → 회원가입 화면으로 이동
                    _uiState.update { it.copy(signupState =
                        AuthState.Error("NEW_USER")) }
                }
            )
            setLoading(false)
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
                emitError(AppError.NotFoundError(e.message ?: "로그아웃 중 오류가 발생했습니다."))
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

    // AuthViewModel.kt에 토큰 검증 함수 추가
    fun validateToken() {
        viewModelScope.launch {
            val hasToken = dataStoreManager.token.first() != null
            if (hasToken) {
                memberRepository.getMyInfo().handleResponse(
                    showErrorToast = false,  // 사용자에게 토스트 메시지 표시하지 않음
                    onSuccess = { _ ->
                        // 토큰이 유효함, 필요한 경우 추가 작업 수행
                        _uiState.update { it.copy(hasToken = true) }
                    },
                    onError = { error ->
                        // 오류가 발생했다면 토큰이 유효하지 않은 것으로 간주
                        if (error is AppError.AuthError) {
                            // 토큰 관련 오류
                            clearTokenAndState()
                        } else {
                            // 네트워크 등 다른 오류 - 토큰은 유지하고 오류만 기록
                        }
                    }
                )
            } else {
                _uiState.update { it.copy(hasToken = false) }
            }
        }
    }

    // 토큰 삭제 및 상태 초기화 함수
    private fun clearTokenAndState() {
        viewModelScope.launch {
            dataStoreManager.clearToken()

            // 다른 ViewModel 상태 초기화
            getKoin().getAll<androidx.lifecycle.ViewModel>().forEach {
                when (it) {
                    is MainViewModel -> it.resetState()
                    is MenuDetailViewModel -> it.resetState()
                    is ReviewViewModel -> it.resetState()
                    is RankingViewModel -> it.resetState()
                    is LikedMenuViewModel -> it.resetState()
                    is NicknameChangeViewModel -> it.resetState()
                    is MyPageViewModel -> it.resetState()
                    // 다른 ViewModel들도 필요에 따라 추가
                }
            }

            _uiState.update {
                it.copy(
                    hasToken = false,
                    signupState = AuthState.Idle,
                    checkNicknameState = AuthState.Idle,
                    logoutState = AuthState.Idle,
                    deleteAccountState = AuthState.Idle,
                    saveTokenState = null
                )
            }
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

    // WebView 이벤트 처리 함수들
    fun onWebViewPageStarted(url: String) {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.PageStarted(url))
        }
    }

    fun onWebViewPageFinished(url: String) {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.PageFinished(url))
        }
    }

    fun onWebViewLoginSuccess(token: String) {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.LoginSuccess(token))
            saveToken(token)
        }
    }

    fun onWebViewSignupSuccess(email: String) {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.SignupSuccess(email))
            setSignupEmail(email)
        }
    }

    fun onWebViewError(message: String) {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.Error(message))
        }
    }

    fun onWebViewRenderProcessGone() {
        viewModelScope.launch {
            _webViewEvents.emit(WebViewEvent.RenderProcessGone)
            showToast("로그인 중 오류가 발생했습니다. 다시 시도해주세요.")
        }
    }
}