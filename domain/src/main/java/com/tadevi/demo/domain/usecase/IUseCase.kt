package com.tadevi.demo.domain.usecase

interface IUseCase<I, O> {
    suspend fun execute(input: I): Result<O>
}

abstract class BaseUseCase<I, O> : IUseCase<I, O> {
    override suspend fun execute(input: I): Result<O> {
        return runCatching { doWork(input) }
    }

    protected abstract suspend fun doWork(input: I): O
}