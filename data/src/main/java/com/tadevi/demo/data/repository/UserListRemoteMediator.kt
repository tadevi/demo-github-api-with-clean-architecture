package com.tadevi.demo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tadevi.demo.data.database.DBUserBrief
import com.tadevi.demo.data.database.UserDatabase
import com.tadevi.demo.data.mapper.toDBUserBrief
import com.tadevi.demo.data.network.UserApiService

@OptIn(ExperimentalPagingApi::class)
class UserListRemoteMediator(
    private val database: UserDatabase,
    private val networkService: UserApiService
) : RemoteMediator<Int, DBUserBrief>() {
    private val userBriefDao = database.userBriefDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBUserBrief>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND ->  state.lastItemOrNull()?.id ?: 0
            }

            val response = networkService.getUserBrief(offset = offset, limit = state.config.pageSize)

            userBriefDao.insertUsers(response.map { it.toDBUserBrief() })

            MediatorResult.Success(endOfPaginationReached = response.size < state.config.pageSize)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}