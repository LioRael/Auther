package cn.echoverse.auther.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime

@Document(collection = "projects")
data class Project(
    @Id
    val id: String?,
    val name: String,
    val description: String,
    val isPrivate: Boolean,
    @DocumentReference
    val owner: User,
    @DocumentReference
    val members: List<User>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)