package cn.echoverse.auther.controller.release

import cn.echoverse.auther.model.File
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

data class FileRequest(
    @field:NotEmpty
    val name: String,
    @field:NotNull
    val size: Long,
    @field:URL
    val url: String,
)

fun FileRequest.toModel() = File(
    name = name,
    size = size,
    url = url,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)