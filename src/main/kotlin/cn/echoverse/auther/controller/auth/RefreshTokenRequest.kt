package cn.echoverse.auther.controller.auth

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token can not be blank.")
    val token: String
)