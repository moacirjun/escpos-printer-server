package com.facilitapix.printers.escpos.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    ServerInstance.server = embeddedServer(Netty, port = 8087, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
