package cn.echoverse.auther.controller.user

import cn.echoverse.auther.model.Profile
import jakarta.validation.constraints.Pattern

data class ProfileRequest(
    @field:Pattern(regexp = "^[a-zA-Z0-9]{4,16}\$", message = "Invalid username")
    val username: String,
)

fun ProfileRequest.toProfile(): Profile = Profile(
    username = username,
    nickname = null,
    bio = null,
    avatar = null,
    phone = null,
)