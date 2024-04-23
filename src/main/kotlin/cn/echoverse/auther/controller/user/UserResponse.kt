package cn.echoverse.auther.controller.user

import cn.echoverse.auther.model.Role
import cn.echoverse.auther.model.User
import cn.echoverse.auther.utils.isAdminOrSelf
import cn.echoverse.auther.utils.isAuthenticating

data class UserResponse(
    val id: String,
    val email: String,
    val profile: ProfileResponse,
    val role: Role,
    val password: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val lastLoginAt: String?
)

fun User.toResponse(full: Boolean = false): UserResponse {
    return if (full || (isAuthenticating() && isAdminOrSelf(email)))
        UserResponse(
            id = id!!,
            email = email,
            password = password,
            profile = profile.toResponse(id),
            role = role,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
            lastLoginAt = lastLoginAt.toString(),
        )
    else
        UserResponse(
            id = id!!,
            email = email,
            password = null,
            profile = profile.toResponse(id),
            role = role,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
            lastLoginAt = lastLoginAt.toString(),
        )
}