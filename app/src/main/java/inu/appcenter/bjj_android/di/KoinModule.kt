package inu.appcenter.bjj_android.di

import android.os.Build
import androidx.annotation.RequiresApi
import inu.appcenter.bjj_android.network.RetrofitAPI
import inu.appcenter.bjj_android.ui.login.LoginViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.mypage.MypageViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.tier.TierViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val viewModelModule = module {
    single { RetrofitAPI.apiService }
//    single<TokenRepository> { TokenRepositoryImpl(get()) }
//
//    single<MemberRepository> { MemberRepositoryImpl(get()) }
//    single<TodoRepository> { TodoRepositoryImpl(get()) }
//    single<CommentRepository> { CommentRepositoryImpl(get()) }
//    single<BucketRepository> { BucketRepositoryImpl(get()) }

    viewModel{ LoginViewModel() }
    viewModel{ MainViewModel() }
    viewModel{ MenuDetailViewModel() }
    viewModel{ MypageViewModel() }
    viewModel{ ReviewViewModel() }
    viewModel{ TierViewModel() }

}