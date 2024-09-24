package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server

import com.facilitapix.printers.escpos.manager.servergui.domain.server.HttpServerInterface
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

object HttpServer : HttpServerInterface {

    private lateinit var server: ApplicationEngine

    override fun start() {
        server = embeddedServer(Netty, port = 8087, host = "0.0.0.0", module = Application::module)
            .start(wait = false)
    }

    override fun stop() {
        if (isRunning()) {
            server.stop(1000, 1000)
        }
    }

    override fun restart() {
        stop()
        start()
    }

    override fun isRunning(): Boolean {
        return this::server.isInitialized
    }
}

fun Application.module() {
    configureRouting()
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        allowHost("localhost:3000", schemes = listOf("http", "https"))
        allowHost("127.0.0.1:3000", schemes = listOf("http", "https"))
        allowHost("172.29.111.56:3000", schemes = listOf("http", "https"))
        allowHost("facilitapix.com", schemes = listOf("http", "https"))
        allowHost("auth.facilitapix.com", schemes = listOf("http", "https"))
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}