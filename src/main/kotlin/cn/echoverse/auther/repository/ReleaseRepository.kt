package cn.echoverse.auther.repository

import cn.echoverse.auther.model.Release
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ReleaseRepository : MongoRepository<Release, String> {

    fun findByProjectId(projectId: String): List<Release>

    fun existsByProjectIdAndTag(projectId: String, tag: String): Boolean

    fun findByProjectIdAndTag(id: String, tag: String): Release?

}