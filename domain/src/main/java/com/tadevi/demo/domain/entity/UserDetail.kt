package com.tadevi.demo.domain.entity

data class UserDetail(
    val username: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val location: String,
    val followers:  Int,
    val following: Int
)