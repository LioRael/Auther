package cn.echoverse.auther.config

import cn.echoverse.auther.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader!!.extractTokenValue()
        val email = try {
            tokenService.extractEmail(token)
        } catch (e: Exception) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token")
            return
        }

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val found = userDetailsService.loadUserByUsername(email)
            val isValid = tokenService.isValid(token, found)

            if (isValid) {
                updateContext(found, request)
            }

            filterChain.doFilter(request, response)
        }
    }

    private fun updateContext(found: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(found, null, found.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun String?.doesNotContainBearerToken(): Boolean = this == null || !startsWith("Bearer ")

    private fun String.extractTokenValue(): String = this.substringAfter("Bearer ")

}