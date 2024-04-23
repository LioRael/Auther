package cn.echoverse.auther.controller.release

import cn.echoverse.auther.model.Release
import cn.echoverse.auther.service.ProjectService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

data class ReleaseRequest(
    @field:Pattern(regexp = "^[a-zA-Z0-9._-]+\$", message = "Invalid tag")
    val tag: String,
    @field:Pattern(regexp = "^[\\s\\S]{0,255}\$", message = "Invalid description")
    val description: String,
    @field:NotBlank
    val projectId: String,
    val files: Map<String, FileRequest>?,
)

fun ReleaseRequest.toModel(projectService: ProjectService) = Release(
    id = null,
    tag = tag,
    description = description,
    project = projectService.findById(projectId) ?: throw ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Project not found"
    ),
    files = files?.mapValues { it.value.toModel() } ?: emptyMap(),
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)