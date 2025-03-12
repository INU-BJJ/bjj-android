package inu.appcenter.bjj_android.ui.mypage.setting.nickname

import android.util.Log
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val UNKNOWN_ERROR = "Unknown error"

// 닉네임 관련 전용 상태 클래스
sealed class NicknameState {
    object Idle : NicknameState()
    object Loading : NicknameState()
    object Success : NicknameState()
    data class Error(val message: String) : NicknameState()
}

data class NicknameChangeUiState(
    val checkNicknameState: NicknameState = NicknameState.Idle,
    val changeNicknameState: NicknameState = NicknameState.Idle,
    val currentNickname: String = ""  // 현재 닉네임 상태 추가
)

class NicknameChangeViewModel(
    private val memberRepository: MemberRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(NicknameChangeUiState())
    val uiState = _uiState.asStateFlow()

    // 초기 닉네임 가져오기
    fun fetchCurrentNickname() {
        viewModelScope.launch {
            setLoading(true)

            memberRepository.getMyInfo().handleResponse(
                onSuccess = { userInfo ->
                    _uiState.update {
                        it.copy(currentNickname = userInfo.nickname)
                    }
                },
                onError = { error ->
                    Log.e("fetchCurrentNickname", error.message ?: UNKNOWN_ERROR)
                    emitError(error)
                }
            )

            setLoading(false)
        }
    }

    // 닉네임 중복 확인
    fun checkNickname(nickname: String) {
        if (nickname.isBlank()) {
            _uiState.update {
                it.copy(checkNicknameState = NicknameState.Error("닉네임을 입력해주세요"))
            }
            return
        }

        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(checkNicknameState = NicknameState.Loading) }

            memberRepository.checkNickname(nickname).handleResponse(
                onSuccess = { isAvailable ->
                    _uiState.update {
                        it.copy(
                            checkNicknameState = if (isAvailable) NicknameState.Success
                            else NicknameState.Error("중복")
                        )
                    }
                },
                onError = { error ->
                    Log.e("checkNickname", error.message ?: UNKNOWN_ERROR)
                    _uiState.update {
                        it.copy(
                            checkNicknameState = NicknameState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )

            setLoading(false)
        }
    }

    // 닉네임 변경
    fun changeNickname(nickname: String) {
        if (_uiState.value.checkNicknameState != NicknameState.Success) {
            return
        }

        viewModelScope.launch {
            setLoading(true)
            _uiState.update { it.copy(changeNicknameState = NicknameState.Loading) }

            memberRepository.modifyNickname(nickname).handleResponse(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            changeNicknameState = NicknameState.Success,
                            currentNickname = nickname  // 성공 시 현재 닉네임 업데이트
                        )
                    }
                },
                onError = { error ->
                    Log.e("changeNickname", error.message ?: UNKNOWN_ERROR)
                    _uiState.update {
                        it.copy(
                            changeNicknameState = NicknameState.Error(error.message ?: UNKNOWN_ERROR)
                        )
                    }
                    emitError(error)
                }
            )

            setLoading(false)
        }
    }

    // 닉네임 중복 확인 상태 초기화
    fun resetNicknameCheckState() {
        _uiState.update {
            it.copy(checkNicknameState = NicknameState.Idle)
        }
    }

    // 닉네임 변경 상태 초기화
    fun resetChangeNicknameState() {
        _uiState.update {
            it.copy(changeNicknameState = NicknameState.Idle)
        }
    }

    // 전체 상태 초기화 (현재 닉네임은 유지)
    fun resetState() {
        val currentNickname = _uiState.value.currentNickname
        _uiState.update {
            NicknameChangeUiState(currentNickname = currentNickname)
        }
    }
}