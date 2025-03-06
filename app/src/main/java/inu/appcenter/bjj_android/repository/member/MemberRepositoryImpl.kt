package inu.appcenter.bjj_android.repository.member

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class MemberRepositoryImpl(private val apiService: APIService) : MemberRepository {
    override suspend fun getAllMembers(): Response<MemberResponseDTO> {
        return apiService.getAllMembers()
    }

    override suspend fun deleteAccount(): Response<Unit> {
        return apiService.deleteAccount()
    }

    override suspend fun signup(signupReq: SignupReq): Response<SignupRes> {
        return apiService.signup(signupReq = signupReq)
    }

    override suspend fun checkNickname(nickname: String): Response<Boolean> {
        return apiService.checkNickname(nickname = nickname)
    }

    override suspend fun modifyNickname(nickname: String): Response<Unit> {
        return apiService.modifyNickname(nickname = nickname)
    }
}