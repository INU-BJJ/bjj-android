package inu.appcenter.bjj_android.di

import inu.appcenter.bjj_android.core.notification.FcmManager
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import inu.appcenter.bjj_android.core.data.remote.RetrofitAPI
import inu.appcenter.bjj_android.core.notification.NotificationManager
import inu.appcenter.bjj_android.feature.main.data.BannerRepository
import inu.appcenter.bjj_android.feature.main.data.BannerRepositoryImpl
import inu.appcenter.bjj_android.feature.menudetail.data.CafeteriasRepository
import inu.appcenter.bjj_android.feature.menudetail.data.CafeteriasRepositoryImpl
import inu.appcenter.bjj_android.feature.profile.data.ItemRepository
import inu.appcenter.bjj_android.feature.profile.data.ItemRepositoryImpl
import inu.appcenter.bjj_android.feature.auth.data.MemberRepository
import inu.appcenter.bjj_android.feature.auth.data.MemberRepositoryImpl
import inu.appcenter.bjj_android.feature.menudetail.data.MenuRepository
import inu.appcenter.bjj_android.feature.menudetail.data.MenuRepositoryImpl
import inu.appcenter.bjj_android.feature.review.data.ReviewRepository
import inu.appcenter.bjj_android.feature.review.data.ReviewRepositoryImpl
import inu.appcenter.bjj_android.feature.main.data.TodayDietRepository
import inu.appcenter.bjj_android.feature.main.data.TodayDietRepositoryImpl
import inu.appcenter.bjj_android.feature.auth.presentation.login.AuthViewModel
import inu.appcenter.bjj_android.feature.main.presentation.MainViewModel
import inu.appcenter.bjj_android.feature.menudetail.presentation.MenuDetailViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.MyPageViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.feature.ranking.presentation.RankingViewModel
import inu.appcenter.bjj_android.feature.review.presentation.ReviewViewModel
import inu.appcenter.bjj_android.core.util.PermissionManager
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
    single<ItemRepository> { ItemRepositoryImpl(get()) }
    single<BannerRepository> { BannerRepositoryImpl(get()) }

}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get(), get()) } // FcmManager 추가
    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
    viewModel { MenuDetailViewModel(get(), get(), get()) }
    viewModel { ReviewViewModel(get(), get(), get()) }
    viewModel { RankingViewModel(get(), get()) }
    viewModel { LikedMenuViewModel(get(), get()) }
    viewModel { NicknameChangeViewModel(get()) }
    viewModel { MyPageViewModel(get()) }
}

// 모든 모듈을 포함하는 목록
val allModules = listOf(appModule, repositoryModule, viewModelModule)