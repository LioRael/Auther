package cn.echoverse.auther.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime

@Document(collection = "ranks")
data class Rank(
    @Id
    val id: String?,
    @DocumentReference
    val project: Project,
    val name: String,
    val description: String,
    val visibility: Visibility,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class Visibility(
    val all: Boolean,
    @DocumentReference
    val categories: List<Release>,
    @DocumentReference(lookup = "{ 'tag': ?#{#target}, 'project': ?#{#root.project} }")
    val tags: List<Release>,
    @DocumentReference(lookup = "{ 'tag': { '\$regex': ?#{#target} }, 'project': ?#{#root.project} }")
    val tagsPattern: List<Release>,
)

@Document(collection = "user_ranks")
data class UserRank(
    @Id
    val id: String?,
    @DocumentReference
    val user: User,
    @DocumentReference
    val rank: Rank,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)