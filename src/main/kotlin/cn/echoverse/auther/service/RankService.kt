package cn.echoverse.auther.service

import cn.echoverse.auther.model.Rank
import cn.echoverse.auther.model.UserRank
import cn.echoverse.auther.repository.RankRepository
import cn.echoverse.auther.repository.UserRankRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class RankService(
    private val rankRepository: RankRepository,
    private val userRankRepository: UserRankRepository,
    private val userService: UserService
) {

    fun findRankByProjectId(projectId: String): List<Rank> = rankRepository.findByProjectId(projectId)

    fun findUserRankByUserIdAndRankId(userId: String, rankId: String): UserRank? =
        userRankRepository.findByUserIdAndRankId(userId, rankId)

    fun findUserRankByUserId(userId: String): List<UserRank> = userRankRepository.findByUserId(userId)

    fun createRank(rank: Rank): Rank {
        val created = rank.copy(
            id = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return rankRepository.save(created)
    }

    fun createUserRank(userRank: UserRank): UserRank {
        val found = findUserRankByUserIdAndRankId(userRank.user.id!!, userRank.rank.id!!)

        return if (found == null) userRankRepository.save(userRank)
        else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User rank already exists")
    }

    fun deleteUserRankByUserIdAndRankId(userId: String, rankId: String) {
        findUserRankByUserIdAndRankId(userId, rankId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "User rank not found"
        )

        userRankRepository.deleteByUserIdAndRankId(userId, rankId)
    }

    fun deleteRankById(id: String) {
        val found = rankRepository.findById(id).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Rank not found"
        )

        rankRepository.delete(found)
    }

    fun updateRank(id: String, rank: Rank): Rank {
        val found = rankRepository.findById(id).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Rank not found"
        )

        val updated = rank.copy(
            id = found.id,
            createdAt = found.createdAt,
        )

        return rankRepository.save(updated)
    }


}