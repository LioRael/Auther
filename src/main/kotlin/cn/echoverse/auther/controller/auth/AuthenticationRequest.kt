package cn.echoverse.auther.controller.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class AuthenticationRequest(
    @field:Email(message = "Invalid email address.")
    val email: String,
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}\$", message = "Invalid password")
    val password: String
)