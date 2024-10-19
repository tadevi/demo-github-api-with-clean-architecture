package com.tadevi.demo.app.koin

import com.google.gson.GsonBuilder
import com.tadevi.demo.data.database.UserDatabase
import com.tadevi.demo.data.network.UserApiService
import com.tadevi.demo.data.repository.UserRepositoryImpl
import com.tadevi.demo.domain.repository.UserRepository
import com.tadevi.demo.domain.usecase.GetUserDetailUseCase
import com.tadevi.demo.domain.usecase.GetUserPagingDataUseCase
import com.tadevi.demo.app.screens.details.UserDetailViewModel
import com.tadevi.demo.app.screens.list.UserListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(UserApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(UserApiService::class.java)
    }

    single {
        UserDatabase.create(get())
    }

    single {
        GsonBuilder().create()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    viewModel {
        UserListViewModel(get())
    }

    viewModel {
        UserDetailViewModel(get())
    }
}

val useCaseModule = module {
    factory { GetUserPagingDataUseCase(get()) }
    factory { GetUserDetailUseCase(get()) }
}