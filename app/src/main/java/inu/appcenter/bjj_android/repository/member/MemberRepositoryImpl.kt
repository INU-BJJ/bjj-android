package inu.appcenter.bjj_android.repository.member

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupDTO
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class MemberRepositoryImpl(private val apiService: APIService) : MemberRepository {
    override suspend fun getAllMembers(): Response<MemberResponseDTO> {
        return apiService.getAllMembers()
    }

    override suspend fun signup(signupDTO: SignupDTO): Response<SignupRes> {
        return apiService.signup(signupDTO = signupDTO)
    }

    override suspend fun checkNickname(nickname: String): Response<Boolean> {
        return apiService.checkNickname(nickname = nickname)
    }

    override suspend fun modifyNickname(nickname: String): Response<Void> {
        return apiService.modifyNickname(nickname = nickname)
    }
}