package inu.appcenter.bjj_android.di

import inu.appcenter.bjj_android.fcm.FcmManager
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.network.RetrofitAPI
import inu.appcenter.bjj_android.notification.NotificationManager
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepositoryImpl
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.repository.member.MemberRepositoryImpl
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.menu.MenuRepositoryImpl
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepositoryImpl
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepositoryImpl
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.mypage.MypageViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.ui.ranking.RankingViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.utils.PermissionManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { DataStoreManager(androidContext()) }
    single { RetrofitAPI(get<DataStoreManager>()) }
    single { get<RetrofitAPI>().apiService }

    // FCM 및 알림 관련 클래스
    single { FcmManager(androidContext(), get(), get()) }
    single { NotificationManager(androidContext()) }
    single { PermissionManager(androidContext(), get()) }
}

val repositoryModule = module {
    single<MemberRepository> { MemberRepositoryImpl(get()) }
    single<TodayDietRepository> { TodayDietRepositoryImpl(get()) }
    single<CafeteriasRepository> { CafeteriasRepositoryImpl(get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get()) }
    single<MenuRepository> { MenuRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get(), get()) } // FcmManager 추가
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { MenuDetailViewModel(get(), get(), get()) }
    viewModel { MypageViewModel() }
    viewModel { ReviewViewModel(get(), get(), get()) }
    viewModel { RankingViewModel(get(), get()) }
    viewModel { LikedMenuViewModel(get(), get()) }
    viewModel { NicknameChangeViewModel(get()) }
}

// 모든 모듈을 포함하는 목록
val allModules = listOf(appModule, repositoryModule, viewModelModule)