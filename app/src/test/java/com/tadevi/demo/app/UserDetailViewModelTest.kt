package com.tadevi.demo.app

import com.tadevi.demo.domain.entity.UserDetail
import com.tadevi.demo.domain.usecase.GetUserDetailUseCase
import com.tadevi.demo.app.screens.details.UserDetailUIState
import com.tadevi.demo.app.screens.details.UserDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UserDetailViewModelTest {
    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var mockUserDetailUseCase: GetUserDetailUseCase

    @Before
    fun setUp() {
        mockUserDetailUseCase = Mockito.mock()
        userDetailViewModel = UserDetailViewModel(mockUserDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadDataSuccess() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val username = "username"
        val expectedData = UserDetail(
            username,
            "avatarUrl",
            "htmlUrl",
            "location",
            10,
            100
        )
        // mock usecase response
        Mockito.`when`(mockUserDetailUseCase.execute(username)).thenReturn(Result.success(expectedData))

        // simulate stateflow fetch & emit data
        val results = mutableListOf<UserDetailUIState>()
        backgroundScope.launch(dispatcher) {
            userDetailViewModel.state.toList(results)
        }
        userDetailViewModel.loadData(username)

        // verify view model result
        assertEquals(
            listOf(UserDetailUIState.Loading, UserDetailUIState.Success(expectedData)),
            results
        )
        verify(mockUserDetailUseCase).execute(username)
    }

    @Test
    fun testLoadDataFail() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val username = "username"

        // mock usecase response
        Mockito.`when`(mockUserDetailUseCase.execute(username)).thenReturn(Result.failure(RuntimeException("Network error")))

        // simulate stateflow fetch & emit data
        val results = mutableListOf<UserDetailUIState>()
        backgroundScope.launch(dispatcher) {
            userDetailViewModel.state.toList(results)
        }
        userDetailViewModel.loadData(username)

        // verify view model result
        assertEquals(
            listOf(UserDetailUIState.Loading, UserDetailUIState.Fail),
            results
        )
        verify(mockUserDetailUseCase).execute(username)
    }
}