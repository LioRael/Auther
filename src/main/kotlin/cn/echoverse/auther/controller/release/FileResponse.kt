package cn.echoverse.auther.controller.release

import cn.echoverse.auther.model.File

data class FileResponse(
    val name: String,
    val size: Long,
    val url: String,
    val createdAt: String,
    val updatedAt: String,
)

fun File.toResponse() = FileResponse(
    name = name,
    size = size,
    url = url,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)