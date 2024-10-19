package com.tadevi.demo.domain.repository

import androidx.paging.PagingData
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.entity.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPagingData(): Flow<PagingData<UserBrief>>

    suspend fun getUserDetail(username: String): UserDetail
}
