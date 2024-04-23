package cn.echoverse.auther.controller.project

import cn.echoverse.auther.controller.user.UserResponse
import cn.echoverse.auther.controller.user.toResponse
import cn.echoverse.auther.model.Project

data class ProjectResponse(
    val id: String,
    val name: String,
    val description: String,
    val isPrivate: Boolean,
    val owner: UserResponse,
    val members: List<UserResponse>,
    val createdAt: String,
    val updatedAt: String
)

fun Project.toResponse() = ProjectResponse(
    id = id!!,
    name = name,
    description = description,
    isPrivate = isPrivate,
    owner = owner.toResponse(),
    members = members.map { it.toResponse() },
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)