package cn.echoverse.auther.controller.user

import cn.echoverse.auther.service.UserService
import cn.echoverse.auther.utils.isAdminOrSelf
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/user")
@Validated
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun create(@RequestBody @Validated userRequest: UserRequest): UserResponse =
        userService.createUser(userRequest.toUser())?.toResponse(true)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user.")

    @GetMapping
    fun findAll(): List<UserResponse> = userService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): UserResponse =
        userService.findById(id)?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<Boolean> {
        val email =
            userService.findById(id)?.email ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
        if (!isAdminOrSelf(email)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied.")
        }
        val result = userService.deleteById(id)
        return if (result) ResponseEntity.ok(true)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
    }

    @GetMapping("/{id}/profile")
    fun findProfile(@PathVariable id: String): ProfileResponse =
        userService.findById(id).let { it?.profile?.toResponse(it.email) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")

    @PutMapping("/{id}/profile")
    fun updateProfile(
        @PathVariable id: String,
        @RequestBody @Validated profileRequest: ProfileRequest
    ): ProfileResponse {
        val email =
            userService.findById(id)?.email ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
        if (!isAdminOrSelf(email)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied.")
        }
        return userService.updateProfile(id, profileRequest.toProfile()).let { it?.profile?.toResponse(it.email) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
    }

}