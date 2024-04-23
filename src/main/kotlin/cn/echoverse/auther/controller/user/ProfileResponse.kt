package cn.echoverse.auther.controller.user

import cn.echoverse.auther.model.Profile
import cn.echoverse.auther.utils.isAdminOrSelf
import cn.echoverse.auther.utils.isAuthenticating

data class ProfileResponse(
    val username: String,
    val nickname: String?,
    val phone: String?,
    val bio: String?,
    val avatar: String?,
)

fun Profile.toResponse(email: String): ProfileResponse {
    return if (isAuthenticating() && isAdminOrSelf(email)) {
        ProfileResponse(
            username = username,
            nickname = nickname,
            bio = bio,
            avatar = avatar,
            phone = phone,
        )
    } else {
        ProfileResponse(
            username = username,
            nickname = nickname,
            bio = bio,
            avatar = avatar,
            phone = null,
        )
    }
}