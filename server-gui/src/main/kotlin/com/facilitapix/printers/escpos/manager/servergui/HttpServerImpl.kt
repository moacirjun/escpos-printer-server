package com.facilitapix.printers.escpos.manager.servergui

import com.facilitapix.printers.escpos.manager.servergui.plugins.configureRouting
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

object ServerInstance {
    lateinit var server: ApplicationEngine

    fun isRunning(): Boolean {
        return this::server.isInitialized
    }
}
class HttpServerImpl : HttpServer {
    override fun start() {
        ServerInstance.server = embeddedServer(Netty, port = 8087, host = "0.0.0.0", module = Application::module)
            .start(wait = false)
    }

    override fun stop() {
        if (ServerInstance.isRunning()) {
            ServerInstance.server.stop(1000, 1000)
        }
    }

    override fun restart() {
        stop()
        start()
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
        allowHost("facilitapix.com", schemes = listOf("http", "https"))
        allowHost("auth.facilitapix.com", schemes = listOf("http", "https"))
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}