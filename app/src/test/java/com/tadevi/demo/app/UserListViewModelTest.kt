package com.tadevi.demo.app

import androidx.paging.PagingData
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.usecase.GetUserPagingDataUseCase
import com.tadevi.demo.app.screens.list.UserListUIState
import com.tadevi.demo.app.screens.list.UserListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class UserListViewModelTest {
    private lateinit var userListViewModel: UserListViewModel
    private lateinit var mockUserPagingDataUseCase: GetUserPagingDataUseCase

    @Before
    fun setUp() {
        mockUserPagingDataUseCase = Mockito.mock()
        userListViewModel = UserListViewModel(mockUserPagingDataUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadDataSuccess() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val users = (1..10).map { UserBrief(username="user$it", "avatar$it", "html$it") }
        val flow = flowOf(PagingData.from(users))

        // mock usecase response
        Mockito.`when`(mockUserPagingDataUseCase.execute(Unit)).thenReturn(Result.success(flow))

        // simulate stateflow fetch & emit data
        val results = mutableListOf<UserListUIState>()
        backgroundScope.launch(dispatcher) {
            userListViewModel.stateFlow.toList(results)
        }
        userListViewModel.loadData()

        // verify view model result
        assertEquals(UserListUIState.Initial, results[0])
        assert(results[1] is UserListUIState.Success)
        verify(mockUserPagingDataUseCase).execute(Unit)
    }


    @Test
    fun testLoadDataFail() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // mock usecase response
        Mockito.`when`(mockUserPagingDataUseCase.execute(Unit)).thenReturn(Result.failure(RuntimeException()))

        // simulate stateflow fetch & emit data
        val results = mutableListOf<UserListUIState>()
        backgroundScope.launch(dispatcher) {
            userListViewModel.stateFlow.toList(results)
        }
        userListViewModel.loadData()

        // verify view model result
        assertEquals(UserListUIState.Initial, results[0])
        assertEquals(UserListUIState.Fail, results[1])
        verify(mockUserPagingDataUseCase).execute(Unit)
    }
}