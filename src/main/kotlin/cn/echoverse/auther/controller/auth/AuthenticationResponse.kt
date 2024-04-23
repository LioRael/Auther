package cn.echoverse.auther.controller.auth

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)