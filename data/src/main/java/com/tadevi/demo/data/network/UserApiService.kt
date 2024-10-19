package com.tadevi.demo.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("/users")
    suspend fun getUserBrief(
        @Query("since") offset: Int,
        @Query("per_page") limit: Int,
    ): List<UserBriefDto>

    @GET("/users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): UserDetailDto

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}