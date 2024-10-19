package com.tadevi.demo.data

import com.tadevi.demo.data.database.DBUserBrief
import com.tadevi.demo.data.mapper.toDBUserBrief
import com.tadevi.demo.data.mapper.toUserBrief
import com.tadevi.demo.data.mapper.toUserDetail
import com.tadevi.demo.data.network.UserBriefDto
import com.tadevi.demo.data.network.UserDetailDto
import org.junit.Test
import org.junit.Assert.*

class MapperTest {
    @Test
    fun testMapperDBUserBrief2UserBrief() {
        val dbUserBrief = DBUserBrief(
            id = 1,
            username = "test-user-name",
            avatarUrl = "avatarUrl/test-user-name",
            htmlUrl = "htmlUrl/test-user-name"
        )
        val result = dbUserBrief.toUserBrief()

        // check user name
        assertEquals(dbUserBrief.username, result.username)
        // check html url
        assertEquals(dbUserBrief.htmlUrl, result.htmlUrl)
        // check avatar url
        assertEquals(dbUserBrief.avatarUrl, result.avatarUrl)
    }

    @Test
    fun testMapperUserBriefDto2DBUserBrief() {
        val userBriefDto = UserBriefDto(
            id = 1,
            login = "test-user-name",
            avatarUrl = "avatarUrl/test-user-name",
            htmlUrl = "htmlUrl/test-user-name"
        )
        val result = userBriefDto.toDBUserBrief()

        // check id
        assertEquals(userBriefDto.id, result.id)
        // check user name
        assertEquals(userBriefDto.login, result.username)
        // check html url
        assertEquals(userBriefDto.htmlUrl, result.htmlUrl)
        // check avatar url
        assertEquals(userBriefDto.avatarUrl, result.avatarUrl)
    }

    @Test
    fun testMapperUserDetailDto2UserDetail() {
        val userDetailDto = UserDetailDto(
            id = 1,
            login = "test-user-name",
            avatarUrl = "avatarUrl/test-user-name",
            htmlUrl = "htmlUrl/test-user-name",
            location = "location/test-user-name",
            followers = 10,
            following = 100,
        )
        val result = userDetailDto.toUserDetail()

        // check user name
        assertEquals(userDetailDto.login, result.username)
        // check html url
        assertEquals(userDetailDto.htmlUrl, result.htmlUrl)
        // check avatar url
        assertEquals(userDetailDto.avatarUrl, result.avatarUrl)
        // check location
        assertEquals(userDetailDto.location, result.location)
        // check followers
        assertEquals(userDetailDto.followers, result.followers)
        // check following
        assertEquals(userDetailDto.following, result.following)
    }
}