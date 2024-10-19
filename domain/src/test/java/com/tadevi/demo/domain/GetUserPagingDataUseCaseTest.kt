package com.tadevi.demo.domain

import androidx.paging.PagingData
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.usecase.GetUserPagingDataUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.mockito.Mockito
import org.mockito.Mockito.verify

import com.tadevi.demo.domain.repository.UserRepository
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetUserPagingDataUseCaseTest {
    lateinit var getUserPagingDataUseCase: GetUserPagingDataUseCase

    @Mock
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        getUserPagingDataUseCase = GetUserPagingDataUseCase(userRepository)
    }

    @Test
    fun `Get User List, return success` (): Unit = runTest {
        val flowItem1 = PagingData.from(listOf(
            UserBrief("user1", "", ""),
            UserBrief("user2", "", ""),
            UserBrief("user3", "", ""),
        ))
        val flowItem2 = PagingData.from(listOf(
            UserBrief("user1", "", ""),
            UserBrief("user2", "", ""),
            UserBrief("user3", "", ""),
            UserBrief("user4", "", ""),
            UserBrief("user5", "", "")
        ))

        val mockResponse = flowOf(flowItem1, flowItem2)
        Mockito.`when`(userRepository.getUserPagingData()).thenReturn(mockResponse)

        // now, just ensure the usecase return the returned result of repository
        // for pagination logic, we will write a more detail testing in data domain
        val userPagingResult = getUserPagingDataUseCase.execute(Unit)
        // verify if the corresponding method in repository is called.
        verify(userRepository).getUserPagingData()
        // verify if the response has the same username with request
        assert(userPagingResult.isSuccess)
        // verify flow count
        assertEquals(mockResponse.count(), userPagingResult.getOrNull()?.count())
        // verify flow result
        assertEquals(mockResponse.toList(), userPagingResult.getOrNull()?.toList())
    }

    @Test
    fun `Get User List, return fail`(): Unit = runTest {
        val errorMessage = "network error"

        Mockito.`when`(userRepository.getUserPagingData()).thenThrow(RuntimeException(errorMessage))

        val userPagingResult = getUserPagingDataUseCase.execute(Unit)
        // verify if the corresponding method in repository is called.
        verify(userRepository).getUserPagingData()
        // verify if the response has the same username with request
        assertEquals(errorMessage, userPagingResult.exceptionOrNull()?.message)
    }
}