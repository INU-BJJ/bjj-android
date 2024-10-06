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

class AuthViewModel(private val memberRepository: MemberRepository, private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState = _signupState.asStateFlow()

    private val _signupEmail = MutableStateFlow("")
    val signupEmail = _signupEmail.asStateFlow()

    fun setSignupEmail(
        email: String
    ){
        _signupEmail.update {
            email
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

    // 토큰 만료 체크 함수 (API 호출 시 사용)
    suspend fun checkTokenExpiration(): Boolean {
        return try {
            // API 호출을 통한 토큰 유효성 검사
            // 예: val response = memberRepository.checkTokenValidity()
            // 여기서는 임시로 true를 반환
            true
        } catch (e: Exception) {
            // 토큰 만료 또는 기타 오류
            false
        }
    }

    fun saveToken(
        token: String
    ){
        viewModelScope.launch {
            dataStoreManager.saveToken(token)
        }
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

    // 회원가입 상태를 초기화하는 함수
    fun resetSignupState() {
        _signupState.value = SignupState.Idle
    }
}