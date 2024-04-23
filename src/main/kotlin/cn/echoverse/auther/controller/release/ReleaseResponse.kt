package cn.echoverse.auther.controller.release

import cn.echoverse.auther.model.Release

data class ReleaseResponse(
    val id: String,
    val projectId: String,
    val version: String,
    val description: String,
    val files: Map<String, FileResponse>,
)

fun Release.toResponse(containFiles: Boolean) = ReleaseResponse(
    id = id!!,
    projectId = project.id!!,
    version = tag,
    description = description,
    files = if (containFiles) files.mapValues { it.value.toResponse() } else emptyMap()
)