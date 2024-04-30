package com.epam.pdf.miniopdftoimage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcChatApplication

fun main(args: Array<String>) {
	runApplication<GrpcChatApplication>(*args)
}
