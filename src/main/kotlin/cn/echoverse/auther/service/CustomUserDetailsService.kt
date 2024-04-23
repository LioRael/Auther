package cn.echoverse.auther.service

import cn.echoverse.auther.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias AutherUser = cn.echoverse.auther.model.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)?.mapToUserDetails()
            ?: throw UsernameNotFoundException("User not found with email: $username")

    private fun AutherUser.mapToUserDetails(): UserDetails = User.builder()
        .username(email)
        .password(password)
        .roles(role.name)
        .build()

}