package inu.appcenter.bjj_android.feature.auth.data

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

interface MemberRepository : BaseRepository {
    suspend fun getMyInfo(): Flow<CustomResponse<MemberResponseDTO>>
    suspend fun signup(signupReq: SignupReq): Flow<CustomResponse<SignupRes>>
    suspend fun deleteAccount(): Flow<CustomResponse<Unit>>
    suspend fun checkNickname(nickname: String): Flow<CustomResponse<Boolean>>
    suspend fun modifyNickname(nickname: String): Flow<CustomResponse<Unit>>
}