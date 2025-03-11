package inu.appcenter.bjj_android.ui.mypage.setting.nickname

import android.util.Log
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.ui.login.AuthState
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val UNKNOWN_ERROR = "Unknown error"

data class NicknameChangeUiState(
    val checkNicknameState: AuthState = AuthState.Idle,
    val changeNicknameState: AuthState = AuthState.Idle
)

class NicknameChangeViewModel(
    private val memberRepository: MemberRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(NicknameChangeUiState())
    val uiState = _uiState.asStateFlow()

    // 닉네임 중복 확인
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
                    Log.e("checkNickname", error.message ?: UNKNOWN_ERROR)
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

    // 닉네임 변경
    fun changeNickname(nickname: String) {
        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(changeNicknameState = AuthState.Loading) }

            memberRepository.modifyNickname(nickname).handleResponse(
                onSuccess = {
                    _uiState.update {
                        it.copy(changeNicknameState = AuthState.Success)
                    }
                },
                onError = { error ->
                    Log.e("changeNickname", error.message ?: UNKNOWN_ERROR)
                    _uiState.update {
                        it.copy(
                            changeNicknameState = AuthState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )
        }
    }

    // 닉네임 중복 확인 상태 초기화
    fun resetNicknameCheckState() {
        _uiState.update {
            it.copy(checkNicknameState = AuthState.Idle)
        }
    }

    // 닉네임 변경 상태 초기화
    fun resetChangeNicknameState() {
        _uiState.update {
            it.copy(changeNicknameState = AuthState.Idle)
        }
    }

    // 전체 상태 초기화
    fun resetState() {
        _uiState.update {
            NicknameChangeUiState()
        }
    }
}