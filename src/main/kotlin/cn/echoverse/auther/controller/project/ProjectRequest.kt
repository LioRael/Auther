package cn.echoverse.auther.controller.project

import cn.echoverse.auther.model.Project
import cn.echoverse.auther.model.User
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

data class ProjectRequest(
    @field:Pattern(regexp = "^\\S.{1,14}\\S$", message = "Invalid name")
    val name: String,
    @field:Pattern(regexp = "^.{0,256}$", message = "Invalid description")
    val description: String,
    @field:NotNull
    val isPrivate: Boolean,
)

fun ProjectRequest.toModel(owner: User) = Project(
    id = null,
    name = name,
    description = description,
    isPrivate = isPrivate,
    owner = owner,
    members = emptyList(),
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)