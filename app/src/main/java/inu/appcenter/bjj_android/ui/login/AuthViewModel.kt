package inu.appcenter.bjj_android.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.member.SignupDTO
import inu.appcenter.bjj_android.repository.member.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    object Success : SignupState()
    data class Error(val message: String) : SignupState()
}

sealed class CheckNicknameState {
    object Idle : CheckNicknameState()
    object Loading : CheckNicknameState()
    object Success : CheckNicknameState()
    data class Error(val message: String) : CheckNicknameState()
}

sealed class LogoutState {
    object Idle : LogoutState()
    object Loading : LogoutState()
    object Success : LogoutState()
    data class Error(val message: String) : LogoutState()
}


class AuthViewModel(private val memberRepository: MemberRepository, private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState = _signupState.asStateFlow()

    private val _saveTokenState = MutableStateFlow<Boolean?>(null)
    val saveTokenState = _saveTokenState.asStateFlow()

    private val _checkNicknameState = MutableStateFlow<CheckNicknameState>(CheckNicknameState.Idle)
    val checkNicknameState = _checkNicknameState.asStateFlow()

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    private val _signupEmail = MutableStateFlow("")
    val signupEmail = _signupEmail.asStateFlow()

    private val _socialName = MutableStateFlow("")
    val socialName = _socialName.asStateFlow()

     fun setSignupEmail(
        email: String
    ){
        _signupEmail.update {
            email
        }
    }

    fun setSocialName(
        social: String
    ){
        _socialName.update {
            social
        }
    }

    private val _hasToken = MutableStateFlow<Boolean?>(null)
    val hasToken = _hasToken.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.token.collect { token ->
                Log.d("check_token", token.toString())
                _hasToken.value = !token.isNullOrEmpty()
            }
        }
    }

    fun saveToken(
        token: String
    ){
        viewModelScope.launch {
            try {
                dataStoreManager.saveToken(token)
                _saveTokenState.value = true
            } catch (e: Exception) {
                _saveTokenState.value = false
            }
        }
    }

    fun resetSaveTokenState() {
        _saveTokenState.value = null
    }

    fun signup(
        signupDTO: SignupDTO
    ){
        viewModelScope.launch {
            try {
                val response = memberRepository.signup(signupDTO = signupDTO)
                if (response.isSuccessful){
                    val token = response.body() ?: throw Exception("회원가입 정보가 비어있습니다.")
                    Log.d("token", token.toString())
                    dataStoreManager.saveToken(token.token)
                    _signupState.value = SignupState.Success
                    _socialName.update { "" }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("signup 실패 원인", e.message.toString())
                _signupState.value = SignupState.Error(e.message ?: "Unknown error")
            } catch (e: RuntimeException){
                Log.e("signup 시간초과", e.message.toString())
                _signupState.value = SignupState.Error("Signup timed out")
            }
        }
    }

    fun checkNickname(
        nickname: String
    ){
        viewModelScope.launch {
            try {
                val response = memberRepository.checkNickname(nickname = nickname)
                if (response.isSuccessful){
                    val checkNickname = response.body() ?: throw Exception("닉네임 검사 정보가 비어있습니다.")
                    Log.d("checkNickname", checkNickname.toString())
                    if (checkNickname){
                        _checkNicknameState.value = CheckNicknameState.Success
                    } else {
                        _checkNicknameState.value = CheckNicknameState.Error(
                            message = "중복"
                        )
                    }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("checkNickname 실패 원인", e.message.toString())
                _checkNicknameState.value = CheckNicknameState.Error(e.message ?: "Unknown error")
            } catch (e: RuntimeException){
                Log.e("checkNickname 시간초과", e.message.toString())
                _checkNicknameState.value = CheckNicknameState.Error("Signup timed out")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading
            try {
                dataStoreManager.clearToken()
                _logoutState.value = LogoutState.Success
                _hasToken.value = false
            } catch (e: Exception) {
                Log.e("logout 실패", e.message.toString())
                _logoutState.value = LogoutState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = LogoutState.Idle
    }


    // 회원가입 상태를 초기화하는 함수
    fun resetSignupState() {
        _signupState.value = SignupState.Idle
    }
}