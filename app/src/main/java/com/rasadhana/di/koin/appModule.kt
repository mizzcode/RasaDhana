package com.rasadhana.di.koin

import androidx.room.Room
import com.rasadhana.data.local.room.RasadhanaDatabase
import com.rasadhana.data.pref.UserPreference
import com.rasadhana.data.pref.dataStore
import com.rasadhana.data.remote.retrofit.ApiConfig
import com.rasadhana.data.repository.RecipeRepository
import com.rasadhana.data.repository.UserRepository
import com.rasadhana.ui.forgotpassword.CreateNewPasswordViewModel
import com.rasadhana.ui.forgotpassword.ForgotPasswordViewModel
import com.rasadhana.ui.home.HomeViewModel
import com.rasadhana.ui.login.LoginViewModel
import com.rasadhana.ui.main.MainViewModel
import com.rasadhana.ui.photo.PhotoViewModel
import com.rasadhana.ui.register.RegisterViewModel
import com.rasadhana.ui.splashscreen.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().dataStore }
    single {
        Room.databaseBuilder(
            androidContext(),
            RasadhanaDatabase::class.java, "Rasadhana.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<RasadhanaDatabase>().userDao() }
    single { get<RasadhanaDatabase>().recipeDao() }
    single { ApiConfig.getApiService() }
    single { ApiConfig.getDummyApiService() }
    single { UserPreference(dataStore = get()) }
    single { RecipeRepository(apiService = get(), dummyApiService = get(), recipeDao = get()) }
    single { UserRepository(apiService = get(), userPreference = get(), userDao = get())}

    viewModel { PhotoViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { CreateNewPasswordViewModel(get()) }
}