package com.tadevi.demo.data.mapper

import com.tadevi.demo.data.network.UserDetailDto
import com.tadevi.demo.domain.entity.UserDetail

fun UserDetailDto.toUserDetail() = UserDetail(
    username = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
    location = location,
    followers = followers,
    following = following
)
