package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.output.PrinterOutputStream
import java.io.FileOutputStream
import java.io.OutputStream
import javax.print.PrintService

object PrinterConnector: PrinterConnectorInterface {

    private var printService: PrintService? = null

    private var fileName: String? = null

    private var outputStream: OutputStream? = null

    private lateinit var escPos: EscPos

    override fun connectToPrinter(printServiceName: String) {
        if (!getAllPrinters().contains(printServiceName)) {
            throw IllegalArgumentException("Printer $printServiceName not found")
        }

        outputStream?.let {
            fileName = null
            it.close()
        }

        connectByPrinterName(printServiceName)
    }

    override fun connectToFile(file: String) {
        outputStream?.let {
            printService = null
            it.close()
        }

        writeToFile(file)
    }

    private fun writeToFile(file: String) {
        fileName = file
        outputStream = FileOutputStream(file, true)
        escPos = EscPos(outputStream)
    }

    private fun connectByPrinterName(printerName: String) {
        printService = PrinterOutputStream.getPrintServiceByName(printerName)
        outputStream = PrinterOutputStream(printService)
        escPos = EscPos(outputStream)
    }

    override fun connectAndRunCommands(commands: EscPos.() -> Unit) {
        escPos = EscPos(instantiateNewOutputStream())
        escPos.commands()
        escPos.close()
    }

    private fun instantiateNewOutputStream(): OutputStream {
        fileName
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                return FileOutputStream(it, true)
            }

        printService?.let {
            return PrinterOutputStream(it)
        }

        throw IllegalStateException("No output stream available. Did you forget to call selectPrinter or selectFile?")
    }

    override fun getAllPrinters(): List<String> = PrinterOutputStream
        .getListPrintServicesNames()
        .toList()
        .toMutableList()
        .apply {
            addAll(
                //fake printers
                listOf("Printer 1", "Printer 2", "Printer 3", "Printer 4", "Printer 5")
            )
        }

    override fun getSelectedPrinter(): String? {
        printService?.let {
            return it.name
        }

        fileName?.let {
            return "Conectado a: $it"
        }

        return null
    }
}