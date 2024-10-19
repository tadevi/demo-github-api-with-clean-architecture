package com.tadevi.demo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tadevi.demo.data.database.UserDatabase
import com.tadevi.demo.data.mapper.toUserBrief
import com.tadevi.demo.data.mapper.toUserDetail
import com.tadevi.demo.data.network.UserApiService
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.entity.UserDetail
import com.tadevi.demo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDatabase: UserDatabase,
    private val networkService: UserApiService,
) : UserRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getUserPagingData(): Flow<PagingData<UserBrief>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_LIMIT),
            remoteMediator = UserListRemoteMediator(userDatabase, networkService),
            pagingSourceFactory = { userDatabase.userBriefDao().userPagingSource() }
        )
            .flow
            .map { pagingData -> pagingData.map { it.toUserBrief()} }
    }

    override suspend fun getUserDetail(username: String): UserDetail {
        return networkService.getUserDetail(username).toUserDetail()
    }

    companion object {
        const val PAGE_LIMIT = 20
    }
}