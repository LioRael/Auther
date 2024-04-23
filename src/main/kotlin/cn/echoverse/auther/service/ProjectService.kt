package cn.echoverse.auther.service

import cn.echoverse.auther.model.Project
import cn.echoverse.auther.model.User
import cn.echoverse.auther.repository.ProjectRepository
import cn.echoverse.auther.utils.isAdmin
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userService: UserService,
) {

    fun createProject(project: Project): Project {
        if (projectRepository.existsByNameAndOwner(project.name, project.owner)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists in your account")
        }
        return projectRepository.save(project)
    }

    fun findAll(): List<Project> = projectRepository.findAll()

    fun findById(id: String): Project? = projectRepository.findById(id).orElse(null)

    fun save(project: Project): Project = projectRepository.save(project)

    fun deleteById(id: String): Boolean {
        val found = findById(id) ?: return false

        projectRepository.delete(found)
        return true
    }

    fun checkProjectOwner(id: String): User {
        val project = findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"
        )
        val current = userService.current()
        if ((project.owner == current) || isAdmin()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this project")
        }

        return project.owner
    }

    fun checkProjectMemberOrOwner(id: String): User {
        val current = userService.current()
        val project = findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"
        )
        val members = project.members + project.owner
        if (current !in members && !isAdmin()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the member of this project")
        }
        return current
    }

    fun findByOwner(owner: User): List<Project> {
        return projectRepository.findByOwner(owner)
    }

    fun findByMembers(member: User): List<Project> {
        return projectRepository.findByMembersContains(member)
    }

}