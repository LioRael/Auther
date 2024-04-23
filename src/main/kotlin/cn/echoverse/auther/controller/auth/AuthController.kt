package cn.echoverse.auther.controller.auth

import cn.echoverse.auther.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping
    fun authenticate(@RequestBody @Validated request: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authenticate(request)

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody @Validated request: RefreshTokenRequest): TokenResponse =
        authenticationService.refreshAccessToken(request.token)?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token!")

    private fun String.mapToTokenResponse(): TokenResponse = TokenResponse(this)

}