package cn.echoverse.auther.controller.user

import cn.echoverse.auther.model.Role
import cn.echoverse.auther.model.User
import cn.echoverse.auther.utils.isAdmin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

data class UserRequest(
    @field:Email(message = "Invalid email address.")
    val email: String,
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}\$", message = "Invalid password")
    val password: String,
    val profile: ProfileRequest,
    val role: Role?,
)

fun UserRequest.toUser(): User = User(
    id = null,
    email = email,
    password = password,
    profile = profile.toProfile(),
    role = if (isAdmin()) role ?: Role.USER else Role.USER,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now(),
    lastLoginAt = null,
)