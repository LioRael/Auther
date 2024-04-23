package cn.echoverse.auther.repository

import cn.echoverse.auther.model.Project
import cn.echoverse.auther.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : MongoRepository<Project, String> {

    fun findByName(projectName: String): Project?

    fun existsByNameAndOwner(name: String, owner: User): Boolean

    fun findByOwner(owner: User): List<Project>

    fun findByMembersContains(member: User): List<Project>

}