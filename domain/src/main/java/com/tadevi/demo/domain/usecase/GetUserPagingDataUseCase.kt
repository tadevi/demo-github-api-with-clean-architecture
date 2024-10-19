package com.tadevi.demo.domain.usecase

import androidx.paging.PagingData
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserPagingDataUseCase(private val repository: UserRepository): BaseUseCase<Unit, Flow<PagingData<UserBrief>>>() {
    override suspend fun doWork(input: Unit): Flow<PagingData<UserBrief>> {
        return repository.getUserPagingData()
    }
}

