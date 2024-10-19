package com.tadevi.demo.data

import androidx.paging.testing.asSnapshot
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.tadevi.demo.data.database.UserDatabase
import com.tadevi.demo.data.repository.UserRepositoryImpl
import com.tadevi.demo.domain.repository.UserRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    private lateinit var userRepository: UserRepository

    private lateinit var userDatabase: UserDatabase

    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setUp() {
        fakeApiService = FakeApiService()

        userDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            UserDatabase::class.java
        ).build()

        userRepository = UserRepositoryImpl(userDatabase, fakeApiService)
    }

    @After
    fun tearDown() {
        fakeApiService.setErrorMessage("")
        fakeApiService.clear()

        userDatabase.clearAllTables()
        userDatabase.close()
    }

    @Test
    fun testGetUserDetailReturnSuccess() = runTest {
        val username = "user1"
        fakeApiService.initUserCount(10)

        val result = userRepository.getUserDetail(username)

        assertEquals(username, result.username)
    }

    @Test
    fun testGetUserDetailReturnFail() = runTest {
        val username = "user1"
        val errorMessage = "Network Error"

        fakeApiService.initUserCount(10)
        fakeApiService.setErrorMessage(errorMessage)

        val exception = assertThrows(RuntimeException::class.java) {
            runBlocking { userRepository.getUserDetail(username) }
        }

        assertEquals(errorMessage, exception.message)
    }

    @Test
    fun testGetUserPagingDataSuccess() = runTest {
        fakeApiService.initUserCount(100)

        val items = userRepository.getUserPagingData()

        val itemsSnapshot = items.asSnapshot {
            scrollTo(index = 50)
        }

        // at least item 40-50 must appear in itemsSnapshot
        assert(itemsSnapshot.map { it.username }.containsAll((40..50).map { "user$it" }))
    }

    @Test
    fun testGetUserPagingDataFail() = runTest {
        fakeApiService.setErrorMessage("Network Error")

        val items = userRepository.getUserPagingData()

        try {
            items
        } catch (e: Exception) {
            throw e
        }
    }
}