package cn.echoverse.auther.service

import cn.echoverse.auther.config.JwtProperties
import cn.echoverse.auther.controller.auth.AuthenticationRequest
import cn.echoverse.auther.controller.auth.AuthenticationResponse
import cn.echoverse.auther.model.RefreshToken
import cn.echoverse.auther.repository.RefreshTokenRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userDetailsService.loadUserByUsername(request.email)
        val userId = userService.findByEmail(request.email)!!.id!!

        val accessToken = generateAccessToken(user, userId)
        val refreshToken = generateRefreshToken(user)

        refreshTokenRepository.save(
            RefreshToken(
                id = null,
                email = request.email,
                token = refreshToken,
                createdAt = LocalDateTime.now()
            )
        )

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun generateAccessToken(userDetails: UserDetails, id: String): String {
        userService.updateLastLogin(userDetails.username)
        return tokenService.generate(
            userDetails = userDetails,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
            additionalClaims = mapOf("id" to id)
        )
    }

    fun generateRefreshToken(userDetails: UserDetails): String =
        tokenService.generate(
            userDetails = userDetails,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
        )

    fun refreshAccessToken(token: String): String? {
        val extractedToken = tokenService.extractEmail(token)

        return extractedToken?.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)
            val refreshTokenInfo = refreshTokenRepository.findByToken(token) ?: return null

            if (!tokenService.isExpired(token) && refreshTokenInfo.email == email)
                generateAccessToken(currentUserDetails, userService.findByEmail(email)!!.id!!)
            else
                null
        }
    }

}