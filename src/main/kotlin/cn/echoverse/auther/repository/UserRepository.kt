package cn.echoverse.auther.repository

import cn.echoverse.auther.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {

    fun findByEmail(email: String): User?

    fun existsByProfileUsernameIgnoreCase(username: String): Boolean

}