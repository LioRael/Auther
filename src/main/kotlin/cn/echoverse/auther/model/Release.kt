package cn.echoverse.auther.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime

@Document(collection = "releases")
data class Release(
    @Id
    val id: String?,
    val tag: String,
    val description: String,
    @DocumentReference
    val project: Project,
    val files: Map<String, File>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class File(
    val name: String,
    val size: Long,
    val url: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)