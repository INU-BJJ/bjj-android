package inu.appcenter.bjj_android.repository.member

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import retrofit2.Response

interface MemberRepository {
    suspend fun getAllMembers() : Response<MemberResponseDTO>

    suspend fun deleteAccount(): Response<Unit>

    suspend fun signup(signupReq: SignupReq) : Response<SignupRes>

    suspend fun checkNickname(nickname : String) : Response<Boolean>

    suspend fun modifyNickname(nickname : String) : Response<Unit>
}