package cn.echoverse.auther.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "users")
data class User(
    @Id
    val id: String?,
    val email: String,
    val password: String,
    val profile: Profile,
    val role: Role,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val lastLoginAt: LocalDateTime?,
)

data class Profile(
    val username: String,
    val nickname: String?,
    val bio: String?,
    val avatar: String?,
    val phone: String?,
)

enum class Role {
    ADMIN,
    USER,
}