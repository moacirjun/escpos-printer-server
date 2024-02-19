package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.github.anastaciocintra.escpos.EscPos

interface PrinterConnectorInterface {

    fun getAllPrinters(): List<String>

    fun connectToPrinter(printServiceName: String)
    fun connectToFile(file: String)
    fun getSelectedPrinter(): String?

    fun connectAndRunCommands(commands: EscPos.() -> Unit)
}