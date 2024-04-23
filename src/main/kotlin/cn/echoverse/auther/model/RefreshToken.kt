package cn.echoverse.auther.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "refresh_tokens")
data class RefreshToken(
    @Id
    val id: String?,
    val email: String,
    val token: String,
    val createdAt: LocalDateTime,
)