package com.tadevi.demo.data

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.tadevi.demo.data.database.DBUserBrief
import com.tadevi.demo.data.database.UserDatabase
import com.tadevi.demo.data.repository.UserListRemoteMediator
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediatorTest {
    private lateinit var apiService: FakeApiService

    private lateinit var userDatabase: UserDatabase

    private lateinit var remoteMediator: UserListRemoteMediator

    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        apiService = FakeApiService()

        userDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            UserDatabase::class.java
        ).build()

        remoteMediator = UserListRemoteMediator(userDatabase, apiService)
    }

    @After
    fun tearDown() {
        userDatabase.clearAllTables()
        apiService.clear()
        apiService.setErrorMessage("")
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = UserListRemoteMediator(userDatabase, apiService)

        apiService.initUserCount(100)

        val pagingState = PagingState<Int, DBUserBrief>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        // users = 100 with initialLoadSize ~ 30 (3 * pageSize) -> endOfPage = false
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenNoMoreData() = runTest {
        val remoteMediator = UserListRemoteMediator(userDatabase, apiService)

        apiService.initUserCount(3)

        val pagingState = PagingState<Int, DBUserBrief>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        // users = 3 with initialLoadSize ~ 30 (3 * pageSize) -> endOfPage = true
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        apiService.setErrorMessage("Network Error")
        apiService.initUserCount(10)

        val pagingState = PagingState<Int, DBUserBrief>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error )
    }
}
