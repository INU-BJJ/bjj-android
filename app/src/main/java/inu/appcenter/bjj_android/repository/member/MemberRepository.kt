package inu.appcenter.bjj_android.repository.member

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface MemberRepository : BaseRepository {
    suspend fun getAllMembers(): Flow<CustomResponse<MemberResponseDTO>>
    suspend fun signup(signupReq: SignupReq): Flow<CustomResponse<SignupRes>>
    suspend fun checkNickname(nickname: String): Flow<CustomResponse<Boolean>>
    suspend fun modifyNickname(nickname: String): Flow<CustomResponse<Unit>>
}