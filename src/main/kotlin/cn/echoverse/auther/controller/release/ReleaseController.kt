package cn.echoverse.auther.controller.release

import cn.echoverse.auther.service.ProjectService
import cn.echoverse.auther.service.ReleaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/releases")
class ReleaseController(
    private val releaseService: ReleaseService,
    private val projectService: ProjectService,
) {

    @GetMapping("/project/{projectId}/latest")
    fun findLatest(
        @PathVariable projectId: String,
        @RequestParam(required = false) tag: String?,
        @RequestParam(required = false) tagsPattern: String?,
        @RequestParam(required = false) category: String?,
    ): ReleaseResponse {
        val project = projectService.findById(projectId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Project not found"
        )
        val release = releaseService.findLatest(project, tag, tagsPattern, category)
        val containFiles = releaseService.isVisibility(release)
        return release.toResponse(containFiles)
    }

    @GetMapping("/project/{projectId}")
    fun findByProjectId(projectId: String) = releaseService.findByProjectId(projectId)

    @GetMapping("/{id}")
    fun findById(id: String): ReleaseResponse {
        val release = releaseService.findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Release not found"
        )
        val containFiles = releaseService.isVisibility(release)
        return releaseService.findById(id)?.toResponse(containFiles) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Release not found"
        )
    }

    @PostMapping
    fun create(@RequestBody @Validated releaseRequest: ReleaseRequest): ReleaseResponse {
        projectService.checkProjectMemberOrOwner(releaseRequest.projectId)
        return releaseService.create(releaseRequest.toModel(projectService)).toResponse(true)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody @Validated releaseRequest: ReleaseRequest): ReleaseResponse {
        projectService.checkProjectMemberOrOwner(releaseService.findById(id)?.project?.id!!)
        val release = releaseService.findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Release not found"
        )
        if (release.tag != releaseRequest.tag) {
            val exists = releaseService.existsByProjectIdAndTag(release.project.id!!, releaseRequest.tag)
            if (exists) {
                throw ResponseStatusException(HttpStatus.CONFLICT, "Release already exists")
            }
        }
        if (release.project.id != releaseRequest.projectId) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Project cannot be changed")
        }
        return releaseService.save(releaseRequest.toModel(projectService).copy(id = id)).toResponse(true)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Boolean> {
        projectService.checkProjectMemberOrOwner(releaseService.findById(id)?.project?.id!!)
        val deleted = releaseService.deleteById(id)
        return if (deleted) ResponseEntity.ok(true)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found")
    }

}