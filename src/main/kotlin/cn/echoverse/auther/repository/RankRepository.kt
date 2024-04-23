package cn.echoverse.auther.repository

import cn.echoverse.auther.model.Rank
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RankRepository : MongoRepository<Rank, String> {

    fun findByProjectId(projectId: String): List<Rank>

}