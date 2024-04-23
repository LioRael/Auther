package cn.echoverse.auther.repository

import cn.echoverse.auther.model.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {

    fun findByToken(token: String): RefreshToken?

}