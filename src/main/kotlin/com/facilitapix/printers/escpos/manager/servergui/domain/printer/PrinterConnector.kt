package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.facilitapix.printers.escpos.manager.servergui.Application
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.output.PrinterOutputStream
import javafx.beans.property.SimpleStringProperty
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.prefs.Preferences
import javax.print.PrintService

object PrinterConnector : PrinterConnectorInterface {

    private var printService: PrintService? = null

    private var fileName: String? = null

    private val preferences = Preferences.userRoot().node(javaClass.simpleName)
    private const val PREFERENCE_KEY = "printerServiceName"

    val printerConnection = SimpleStringProperty()

    override fun connectToPrinter(printServiceName: String) {
        if (Application.isDevModeEnabled && "Testing Printer " in printServiceName) {
            connectToFile("testing.bin")
            preferences.put(PREFERENCE_KEY, printServiceName)
            return
        }

        if (!getAllPrinters().contains(printServiceName)) {
            throw IllegalArgumentException("Printer $printServiceName not found")
        }

        fileName = null
        printService = PrinterOutputStream.getPrintServiceByName(printServiceName)
        printerConnection.value = PrinterConnectionStatus.CONNECTED.name
        preferences.put(PREFERENCE_KEY, printServiceName)
    }

    override fun connectToFile(file: String) {
        fileName = file
        printService = null
        printerConnection.value = PrinterConnectionStatus.NOT_CONNECTED.name
        preferences.remove(PREFERENCE_KEY)
    }

    override fun connectAndRunCommands(commands: EscPos.() -> Unit) {
        EscPos(instantiateNewOutputStream()). apply {
            commands()
            close()
        }
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

    enum class PrinterConnectionStatus {
        CONNECTED,
        NOT_CONNECTED
    }
}