package com.tadevi.demo.data

import android.content.Context
import androidx.paging.*
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.tadevi.demo.data.database.DBUserBrief
import com.tadevi.demo.data.database.UserDao
import com.tadevi.demo.data.database.UserDatabase
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class DatabaseTest {
    private lateinit var userDatabase: UserDatabase

    private lateinit var appContext: Context

    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        userDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            UserDatabase::class.java
        ).build()

        userDao = userDatabase.userBriefDao()
    }

    @After
    fun tearDown() {
        userDatabase.close()
    }

    @Test
    fun testInsertUser() = runTest {
        val user = DBUserBrief(
            id = 1,
            username = "user1",
            avatarUrl = "avatarUrl/1",
            htmlUrl = "htmlUrl/1"
        )
        userDao.insertUsers(listOf(user))

        val byName = userDao.getUserById(user.id)

        assertThat(byName, equalTo(user))
    }

    @Test
    fun testPageSource() = runTest {
        val pagingSource = userDao.userPagingSource()

        val mockUsers = (1 until 100).map { createDBUserBrief(it) }

        userDao.insertUsers(mockUsers)

        val pager = TestPager(PagingConfig(pageSize = 20), pagingSource)

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assert(mockUsers.containsAll(result.data))
    }

    private fun createDBUserBrief(id: Int): DBUserBrief {
        return DBUserBrief(
            id = id,
            username = "user$id",
            avatarUrl = "avatarUrl/$id",
            htmlUrl = "htmlUrl/$id"
        )
    }
}
