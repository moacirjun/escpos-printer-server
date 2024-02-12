package com.facilitapix.printers.escpos.server.domain

import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.output.PrinterOutputStream
import java.io.FileOutputStream
import java.io.OutputStream
import javax.print.PrintService
import kotlin.system.exitProcess

object PrinterConnector {

    private lateinit var printService: PrintService

    private lateinit var fileName: String

    private lateinit var outputStream : OutputStream

    lateinit var escPos: EscPos
        private set

    fun initialize(applicationArgs: Array<String>) {
        val printerName = if (applicationArgs.isNotEmpty()) applicationArgs[0] else "file://testing.bin"
        val printersList = getAllPrinters()

        if (printerName.startsWith("file://")) {
            writeToFile(printerName.replace("file://", ""))
            println("Printing to file ${printerName.replace("file://", "")}")
            return
        }

        if (printersList.isEmpty()) {
            println("No printers available. Please check the printer connection and try again.")
            exitProcess(313)
        }

        if (!printersList.contains(printerName)) {
            println("The printer $printerName is not available. Pleas check the printer connection and try again.")
            println("Available printers:")
            printersList.forEach { println("  - $it") }

            exitProcess(314)
        }

        connectByPrinterName(printerName)
    }

    private fun writeToFile(file: String) {
        fileName = file
        outputStream = FileOutputStream(fileName, true)
        escPos = EscPos(outputStream)
    }

    private fun connectByPrinterName(printerName: String) {
        printService = PrinterOutputStream.getPrintServiceByName(printerName)
        outputStream = PrinterOutputStream(printService)
        escPos = EscPos(outputStream)
    }

    fun disconnect() {
        escPos.close()
    }

    fun connectAndRunCommands(commands: EscPos.() -> Unit) {
        escPos = EscPos(instantiateNewOutputStream())
        escPos.commands()
        escPos.close()
    }

    private fun instantiateNewOutputStream(): OutputStream {
        return if (::fileName.isInitialized && fileName.isNotEmpty()) {
            FileOutputStream(fileName, true)
        } else if (::printService.isInitialized) {
            PrinterOutputStream(printService)
        } else {
            throw IllegalStateException("No output stream available")
        }
    }

    fun getAllPrinters(): List<String> = PrinterOutputStream.getListPrintServicesNames().toList()
}