package cn.echoverse.auther.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auther.jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,
)