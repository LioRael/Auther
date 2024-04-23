package cn.echoverse.auther.service

import cn.echoverse.auther.model.Project
import cn.echoverse.auther.model.Release
import cn.echoverse.auther.repository.ReleaseRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ReleaseService(
    private val releaseRepository: ReleaseRepository,
    private val rankService: RankService,
    private val userService: UserService
) {

    fun findById(id: String): Release? {
        return releaseRepository.findById(id).orElse(null)
    }

    fun findByProjectId(projectId: String): List<Release> {
        return releaseRepository.findByProjectId(projectId)
    }

    fun findLatestByProjectId(projectId: String): Release? {
        return releaseRepository.findByProjectId(projectId).maxByOrNull { it.createdAt }
    }

    fun create(release: Release): Release {
        val exists = releaseRepository.existsByProjectIdAndTag(release.project.id!!, release.tag)
        if (exists) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Release already exists")
        }

        return releaseRepository.save(release)
    }

    fun save(release: Release): Release = releaseRepository.save(release)

    fun deleteById(id: String): Boolean {
        val found = findById(id) ?: return false

        releaseRepository.delete(found)
        return true
    }

    fun existsByProjectIdAndTag(id: String, tag: String): Boolean = releaseRepository.existsByProjectIdAndTag(id, tag)

    fun findLatest(project: Project, tag: String?, tagsPattern: String?, category: String?): Release {
        return if (tag != null) {
            releaseRepository.findByProjectIdAndTag(project.id!!, tag)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found")
        } else if (category != null) {
            releaseRepository.findByProjectId(project.id!!).sortedByDescending { it.createdAt }.firstOrNull {
                it.files.keys.contains(category)
            } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found")
        } else if (tagsPattern != null) {
            releaseRepository.findByProjectId(project.id!!).sortedByDescending { it.createdAt }.firstOrNull {
                it.tag.matches(tagsPattern.toRegex())
            } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found")
        } else {
            findLatestByProjectId(project.id!!) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Release not found"
            )
        }
    }

    fun isVisibility(release: Release): Boolean {
        try {
            checkVisibility(release)
            return true
        } catch (e: ResponseStatusException) {
            return false
        }
    }

    fun checkVisibility(release: Release) {
        val user = userService.current()
        val userRanks = rankService.findUserRankByUserId(user.id!!)
        val userRank = userRanks.find { it.rank.project.id == release.project.id }
        val rank = userRank?.rank ?: throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "You are not the member of this project"
        )

        if (rank.visibility.all)
            return
        if (rank.visibility.tags.contains(release))
            return
        if (rank.visibility.categories.contains(release))
            return
        if (rank.visibility.tagsPattern.contains(release))
            return

        throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this release")
    }

}