package com.tadevi.demo.data.network

import com.google.gson.annotations.SerializedName

data class UserDetailDto(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("login")
    val login: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("html_url")
    val htmlUrl: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("followers")
    val followers: Int = 0,
    @SerializedName("following")
    val following: Int = 0,
)
