package cn.echoverse.auther.utils

import cn.echoverse.auther.model.Role
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.server.ResponseStatusException

fun context(block: (SecurityContext) -> Unit = {}): SecurityContext = SecurityContextHolder.getContext().apply(block)

fun isAdmin(): Boolean = context().authentication.authorities.any { it.authority == "ROLE_${Role.ADMIN.name}" }

fun isAdminOrSelf(email: String): Boolean {
    val user = try {
        getAuthenticatedUser()
    } catch (e: RuntimeException) {
        return false
    }
    return isAdmin() || user.username == email
}

fun isAuthenticating(): Boolean = context().authentication.isAuthenticated

fun getAuthenticatedUser(): UserDetails = if (!isAuthenticating()) {
    throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authenticated")
} else {
    context().authentication.principal as UserDetails
}

fun checkAuthentication() {
    if (!isAuthenticating()) {
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authenticated")
    }
}