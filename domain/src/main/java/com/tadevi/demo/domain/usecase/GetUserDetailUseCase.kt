package com.tadevi.demo.domain.usecase

import com.tadevi.demo.domain.entity.UserDetail
import com.tadevi.demo.domain.repository.UserRepository


class GetUserDetailUseCase(private val repository: UserRepository): BaseUseCase<String, UserDetail>() {
    override suspend fun doWork(input: String): UserDetail {
        return repository.getUserDetail(input)
    }
}