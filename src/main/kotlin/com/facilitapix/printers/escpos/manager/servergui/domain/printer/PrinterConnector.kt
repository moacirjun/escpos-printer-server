package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.facilitapix.printers.escpos.manager.servergui.Application
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.output.PrinterOutputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.prefs.Preferences
import javax.print.PrintService

object PrinterConnector : PrinterConnectorInterface {

    private var printService: PrintService? = null

    private var fileName: String? = null

    private var outputStream: OutputStream? = null

    private lateinit var escPos: EscPos

    private val preferences = Preferences.userRoot().node(javaClass.simpleName)
    private const val PREFERENCE_KEY = "printerServiceName"

    override fun connectToPrinter(printServiceName: String) {
        if (Application.isDevModeEnabled && "Testing Printer " in printServiceName) {
            connectToFile("testing.bin")
            preferences.put(PREFERENCE_KEY, printServiceName)
            return
        }

        if (!getAllPrinters().contains(printServiceName)) {
            throw IllegalArgumentException("Printer $printServiceName not found")
        }

        outputStream?.let {
            fileName = null
            it.close()
        }

        connectByPrinterName(printServiceName)
        preferences.put(PREFERENCE_KEY, printServiceName)
    }

    override fun connectToFile(file: String) {
        outputStream?.let {
            printService = null
            it.close()
        }

        writeToFile(file)
        preferences.remove(PREFERENCE_KEY)
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

    override fun getAllPrinters(): List<String> =
        PrinterOutputStream
            .getListPrintServicesNames()
            .toList()
            .apply {
                if (Application.isDevModeEnabled) {
                    toMutableList().apply {
                        add("Testing Printer 1")
                        add("Testing Printer 2")
                        add("Testing Printer 3")
                        add("Testing Printer 4")
                        add("Testing Printer 5")
                    }
                }
            }

    override fun connectToPersistedPrinter() {
        getPersistedPrinter()?.let {
            return connectToPrinter(it)
        }

        throw IllegalStateException("No printer was persisted")
    }

    override fun getPersistedPrinter(): String? = preferences.get(PREFERENCE_KEY, null)

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