package cn.echoverse.auther.repository

import cn.echoverse.auther.model.UserRank
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRankRepository : MongoRepository<UserRank, String> {

    fun findByUserId(userId: String): List<UserRank>
    fun findByRankId(rankId: String): List<UserRank>
    fun findByUserIdAndRankId(userId: String, rankId: String): UserRank?
    fun deleteByUserIdAndRankId(userId: String, rankId: String)

}