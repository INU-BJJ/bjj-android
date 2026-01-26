package inu.appcenter.bjj_android.feature.auth.data

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class MemberRepositoryImpl(private val apiService: APIService) : MemberRepository {
    override suspend fun getMyInfo(): Flow<CustomResponse<MemberResponseDTO>> = safeApiCall {
        apiService.getAllMembers()
    }

    override suspend fun deleteAccount(): Flow<CustomResponse<Unit>> = safeApiCall {
        apiService.deleteAccount()
    }

    override suspend fun signup(signupReq: SignupReq): Flow<CustomResponse<SignupRes>> = safeApiCall {
        apiService.signup(signupReq = signupReq)
    }

    override suspend fun checkNickname(nickname: String): Flow<CustomResponse<Boolean>> = safeApiCall {
        apiService.checkNickname(nickname = nickname)
    }

    override suspend fun modifyNickname(nickname: String): Flow<CustomResponse<Unit>> = safeApiCall {
        apiService.modifyNickname(nickname = nickname)
    }
}