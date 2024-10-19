package com.tadevi.demo.data.mapper

import com.tadevi.demo.data.database.DBUserBrief
import com.tadevi.demo.data.network.UserBriefDto
import com.tadevi.demo.domain.entity.UserBrief

fun DBUserBrief.toUserBrief() = UserBrief(
    username = username,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
)

fun UserBriefDto.toDBUserBrief() = DBUserBrief(
    id = id,
    username = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
)
