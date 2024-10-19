package com.tadevi.demo.data

import com.tadevi.demo.data.network.UserApiService
import com.tadevi.demo.data.network.UserBriefDto
import com.tadevi.demo.data.network.UserDetailDto
import java.util.concurrent.ThreadLocalRandom

class FakeApiService : UserApiService {
    private val users = mutableListOf<UserDetailDto>()
    private var errorMessage = ""

    fun initUserCount(count: Int) {
        users.clear()
        users.addAll(
            (0 ..count).map { id ->  UserDetailDto(
                id,
                "user$id",
                "avatarUrl/$id",
                "htmlUrl/$id",
                "location/$id",
                ThreadLocalRandom.current().nextInt(0, 1000),
                ThreadLocalRandom.current().nextInt(0, 1000)
            )
            }
        )
    }

    override suspend fun getUserBrief(offset: Int, limit: Int, token: String): List<UserBriefDto> {
        if (errorMessage.isNotEmpty()) throw RuntimeException(errorMessage)
        val start = offset + 1
        val end = Math.min(users.size, start + limit)
        if (start >= end) return emptyList()

        return users.subList(start, end).map { UserBriefDto(it.id, it.login, it.avatarUrl, it.htmlUrl) }
    }

    override suspend fun getUserDetail(username: String): UserDetailDto {
        if (errorMessage.isNotEmpty()) throw RuntimeException(errorMessage)
        return users.first { it.login == username }
    }

    fun setErrorMessage(msg: String) {
        errorMessage = msg
    }

    fun clear() {
        users.clear()
    }
}