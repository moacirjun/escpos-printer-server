package com.facilitapix

import com.facilitapix.domain.PrinterConnector
import com.facilitapix.domain.getAllPrinters
import com.facilitapix.plugins.*
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val printers = getAllPrinters()

    if (printers.isEmpty()) {
        println("Nenhuma impressora encontrada, deseja continuar e gravar tudo em um arquivo? (s/n)")
        val answer = readlnOrNull()
        if (answer?.toLowerCase() != "s") {
            exitProcess(0)
        } else {
            PrinterConnector.initialize(arrayOf("file://testing.bin"))
        }
    } else {
        println("Impressoras disponíveis:")
        printers.forEachIndexed { index, printer -> println("  [${index + 1}] $printer") }
        val selectedIndex = readlnOrNull()?.toInt() ?: 1
        val selectedPrinter = printers.getOrNull(selectedIndex - 1)

        if (selectedPrinter == null) {
            println("Impressora não encontrada")
            PrinterConnector.initialize(arrayOf(""))
        } else {
            PrinterConnector.initialize(arrayOf(selectedPrinter))
        }
    }

    embeddedServer(Netty, port = 8087, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
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
