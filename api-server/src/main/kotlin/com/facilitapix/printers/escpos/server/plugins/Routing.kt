package com.facilitapix.printers.escpos.server.plugins

import com.facilitapix.printers.escpos.server.domain.OrderReceipt
import com.facilitapix.printers.escpos.server.domain.getAllPrinters
import com.facilitapix.printers.escpos.server.domain.printOrderReceipt
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/show-printers") {
            call.respond(getAllPrinters())
        }

        post("/print-qr-code-receipt") {
            val receipt = call.receive<OrderReceipt>()
            printOrderReceipt(receipt)

            call.respond("""{"message": "OK"}""")
        }
    }
}
