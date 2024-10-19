package com.tadevi.demo.data.database

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<DBUserBrief>)

    @Query("SELECT * FROM user_brief ORDER BY id ASC")
    fun userPagingSource(): PagingSource<Int, DBUserBrief>

    @VisibleForTesting
    @Query("SELECT * FROM user_brief WHERE id = :id")
    suspend fun getUserById(id: Int): DBUserBrief?
}