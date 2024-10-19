package com.tadevi.demo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [DBUserBrief::class])
abstract class UserDatabase: RoomDatabase() {
    abstract fun userBriefDao(): UserDao

    companion object {
        private const val DB_NAME = "db_user"

        fun create(context: Context): UserDatabase {
            return Room
                .databaseBuilder(context, UserDatabase::class.java, DB_NAME)
                .build()
        }
    }
}