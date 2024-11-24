package com.rasadhana.di.koin

import com.rasadhana.data.remote.retrofit.ApiConfig
import com.rasadhana.data.repository.RecipeRepository
import com.rasadhana.data.repository.UserRepository
import com.rasadhana.ui.login.LoginViewModel
import com.rasadhana.ui.photo.PhotoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ApiConfig.getApiService() }
    single { RecipeRepository(apiService = get()) }
    single { UserRepository(apiService = get()) }

    viewModel { PhotoViewModel() }
    viewModel { LoginViewModel() }
}