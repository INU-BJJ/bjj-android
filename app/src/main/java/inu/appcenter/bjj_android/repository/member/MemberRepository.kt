package inu.appcenter.bjj_android.repository.member

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupDTO
import inu.appcenter.bjj_android.model.member.SignupRes
import retrofit2.Response

interface MemberRepository {
    suspend fun getAllMembers() : Response<MemberResponseDTO>

    suspend fun signup(signupDTO: SignupDTO) : Response<SignupRes>
}