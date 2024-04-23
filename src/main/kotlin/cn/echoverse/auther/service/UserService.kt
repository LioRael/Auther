package cn.echoverse.auther.service

import cn.echoverse.auther.model.Profile
import cn.echoverse.auther.model.User
import cn.echoverse.auther.repository.UserRepository
import cn.echoverse.auther.utils.getAuthenticatedUser
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) {

    fun createUser(user: User): User? {
        val found = userRepository.findByEmail(user.email)
        val exists = userRepository.existsByProfileUsernameIgnoreCase(user.profile.username)

        return if (found == null && !exists)
            save(user)
        else null
    }

    fun findById(id: String): User? = userRepository.findById(id).orElse(null)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun findAll(): List<User> = userRepository.findAll()

    fun deleteById(id: String): Boolean {
        val found = findById(id) ?: return false

        userRepository.delete(found)
        return true
    }

    fun save(user: User): User {
        val updated = user.copy(password = encoder.encode(user.password))

        return userRepository.save(updated)
    }

    fun updateProfile(id: String, toProfile: Profile): User? {
        val found = findById(id) ?: return null
        if (toProfile.username != found.profile.username && userRepository.existsByProfileUsernameIgnoreCase(toProfile.username))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.")

        val updated = found.copy(
            profile = toProfile,
            id = found.id,
            email = found.email,
            password = found.password,
            role = found.role,
            createdAt = found.createdAt,
            updatedAt = found.updatedAt,
            lastLoginAt = found.lastLoginAt,
        )

        return save(updated)
    }

    fun updateLastLogin(email: String) {
        val found = findByEmail(email) ?: return

        val updated = found.copy(lastLoginAt = LocalDateTime.now())
        userRepository.save(updated)
    }

    fun current(): User {
        val email = getAuthenticatedUser().username
        return findByEmail(email) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

}