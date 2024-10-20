package com.tadevi.demo.data.network

import com.google.gson.annotations.SerializedName

data class UserBriefDto(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("login")
    val login: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("html_url")
    val htmlUrl: String = "",
)