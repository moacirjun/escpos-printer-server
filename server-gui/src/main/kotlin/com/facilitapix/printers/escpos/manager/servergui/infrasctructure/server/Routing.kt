package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterService
import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val printerService = PrinterService()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/show-printers") {
            call.respond(printerService.getAllPrinters())
        }

        post("/print-qr-code-receipt") {
            val receipt = call.receive<OrderReceipt>()
            printerService.printOrderReceipt(receipt)

            call.respond("""{"message": "OK"}""")
        }
    }
}
