package com.tadevi.demo.domain

import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.entity.UserDetail
import com.tadevi.demo.domain.repository.UserRepository
import com.tadevi.demo.domain.usecase.GetUserDetailUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetUserDetailUseCaseTest {
    lateinit var getUserDetailUseCase: GetUserDetailUseCase

    @Mock
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        getUserDetailUseCase = GetUserDetailUseCase(userRepository)
    }

    @Test
    fun `Get User Detail, return success` (): Unit = runTest {
        val username = "user1"
        val response = UserDetail(
            username,
            "https://test.com/avatar/1",
            "https://github.com",
            "vietnam",
            1,
            1
        )
        val result = listOf(
            UserBrief("user1","https://test.com/avatar/1", "https://github.com"),
            UserBrief("user1","https://test.com/avatar/1", "https://github.com")
        )
        Mockito.`when`(userRepository.getUserDetail(username)).thenReturn(response)
        val userDetail = getUserDetailUseCase.execute(username)

        // verify if the corresponding method in repository is called.
        verify(userRepository).getUserDetail(username)
        // verify if the response has the same username with request
        assert(userDetail.isSuccess)
        assertEquals(username, userDetail.getOrNull()?.username)
    }

    @Test
    fun `Get User Detail, return fail`(): Unit = runTest {
        val username = "user1"
        val errorMessage = "network error"

        Mockito.`when`(userRepository.getUserDetail(username)).thenThrow(RuntimeException(errorMessage))

        val userDetail = getUserDetailUseCase.execute(username)

        // verify if the corresponding method in repository is called.
        verify(userRepository).getUserDetail(username)
        // verify if the response has the same username with request
        assertEquals(errorMessage, userDetail.exceptionOrNull()?.message)
    }
}