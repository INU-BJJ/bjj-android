package inu.appcenter.bjj_android.di

import android.os.Build
import androidx.annotation.RequiresApi
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.network.RetrofitAPI
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepositoryImpl
import inu.appcenter.bjj_android.repository.member.MemberRepository
import inu.appcenter.bjj_android.repository.member.MemberRepositoryImpl
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepositoryImpl
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.mypage.MypageViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.tier.TierViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { DataStoreManager(androidContext()) }
    single { RetrofitAPI(get<DataStoreManager>()) }
    single { get<RetrofitAPI>().apiService }

    single<MemberRepository> { MemberRepositoryImpl(get()) }
    single<TodayDietRepository> { TodayDietRepositoryImpl(get()) }
    single<CafeteriasRepository> { CafeteriasRepositoryImpl(get()) }


    viewModel{ AuthViewModel(get(), get()) }
    viewModel{ MainViewModel(get(), get()) }
    viewModel{ MenuDetailViewModel() }
    viewModel{ MypageViewModel() }
    viewModel{ ReviewViewModel() }
    viewModel{ TierViewModel() }

}