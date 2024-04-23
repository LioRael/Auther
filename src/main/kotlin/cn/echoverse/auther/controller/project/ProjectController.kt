package cn.echoverse.auther.controller.project

import cn.echoverse.auther.controller.user.UserResponse
import cn.echoverse.auther.controller.user.toResponse
import cn.echoverse.auther.service.ProjectService
import cn.echoverse.auther.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService,
    private val userService: UserService
) {

    @GetMapping
    fun findAll(): List<ProjectResponse> = projectService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ProjectResponse =
        projectService.findById(id)?.toResponse() ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"
        )

    @GetMapping("/owner")
    fun findByOwner(): List<ProjectResponse> {
        val owner = userService.current()
        return projectService.findByOwner(owner).map { it.toResponse() }
    }

    @GetMapping("/members")
    fun findByMembers(): List<ProjectResponse> {
        val member = userService.current()
        return projectService.findByMembers(member).map { it.toResponse() }
    }

    @GetMapping("/{id}/members")
    fun findMembers(@PathVariable id: String): List<UserResponse> =
        projectService.findById(id)?.members?.map { it.toResponse() } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"
        )

    @GetMapping("/{id}/owner")
    fun findOwner(@PathVariable id: String): UserResponse =
        projectService.findById(id)?.owner?.toResponse() ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"
        )

    @PostMapping
    fun create(@RequestBody @Validated request: ProjectRequest): ProjectResponse {
        val current = userService.current()
        val project = request.toModel(current)
        return projectService.createProject(project).toResponse()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody @Validated request: ProjectRequest): ProjectResponse {
        val owner = projectService.checkProjectOwner(id)
        val updatedProject = request.toModel(owner).copy(id = id)
        return projectService.save(updatedProject).toResponse()
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<Boolean> {
        projectService.checkProjectOwner(id)
        val result = projectService.deleteById(id)
        return if (result) ResponseEntity.ok(true)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.")
    }

}