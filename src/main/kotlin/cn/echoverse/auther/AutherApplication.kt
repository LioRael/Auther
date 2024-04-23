package cn.echoverse.auther

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AutherApplication

fun main(args: Array<String>) {
    runApplication<AutherApplication>(*args)
}