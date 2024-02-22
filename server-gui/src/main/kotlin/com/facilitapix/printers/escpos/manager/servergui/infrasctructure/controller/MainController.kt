package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller

import com.facilitapix.printers.escpos.manager.servergui.domain.SystemStatus
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server.HttpServer
import com.facilitapix.printers.escpos.manager.servergui.loadView
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle


class MainController {

    @FXML
    private lateinit var selectedPrinterLbl: Label

    @FXML
    private lateinit var changeSelectedPrinterBtn: Button

    @FXML
    private lateinit var printExampleReceiptBtn: Button

    @FXML
    private lateinit var serverStatusLbl: Label

    @FXML
    private lateinit var restartServerBtn: Button

    @FXML
    private lateinit var fetchServerStatus: Button

    @FXML
    lateinit var printerStatusCircle: Circle
    @FXML
    lateinit var printerStatusCirclePulse: Circle
    private val printerStatusIndicator by lazy {
        StatusIndicator(printerStatusCircle, printerStatusCirclePulse)
    }

    @FXML
    lateinit var serverStatusCircle: Circle
    @FXML
    lateinit var serverStatusCirclePulse: Circle
    private val serverStatusIndicator by lazy {
        StatusIndicator(serverStatusCircle, serverStatusCirclePulse)
    }

    private var systemStatus: SystemStatus = SystemStatus(
        serverStatus = SimpleStringProperty("Iniciando integrador..."),
        selectedPrinter = SimpleStringProperty("Iniciando integrador...")
    )

    companion object {
        const val NO_PRINTER_CONFIGURED_MESSAGE = "Nenhuma impressora selecionada"
        const val SERVER_NOT_RUNNING_MESSAGE = "Offline"
        const val SERVER_RUNNING_MESSAGE = "Online"

        fun instantiateScene() = Scene(loadView<VBox>("main-view.fxml")).apply {
            fill = Color.TRANSPARENT
        }
    }

    @FXML
    private fun initialize() {
        selectedPrinterLbl.textProperty().bind(systemStatus.selectedPrinter)
        serverStatusLbl.textProperty().bind(systemStatus.serverStatus)

        printerStatusIndicator.startAnimation()
        serverStatusIndicator.startAnimation()

        updateSystemStatus()
    }

    private fun updateSystemStatus() {
        systemStatus.serverStatus.value = if (HttpServer.isRunning()) {
            serverStatusIndicator.success()
            SERVER_RUNNING_MESSAGE
        } else {
            serverStatusIndicator.error()
            SERVER_NOT_RUNNING_MESSAGE
        }

        systemStatus.selectedPrinter.value = PrinterConnector.getSelectedPrinter() ?: NO_PRINTER_CONFIGURED_MESSAGE
        if (systemStatus.selectedPrinter.value != NO_PRINTER_CONFIGURED_MESSAGE) {
            printerStatusIndicator.success()
            changeSelectedPrinterBtn.text = "Alterar"
            printExampleReceiptBtn.isVisible = true
        } else {
            systemStatus.selectedPrinter.value = NO_PRINTER_CONFIGURED_MESSAGE
            printerStatusIndicator.error()
            changeSelectedPrinterBtn.text = "Selecionar"
            printExampleReceiptBtn.isVisible = false
        }
    }

    @FXML
    private fun handleChangeSelectedPrinterBtnClick() {
        PrinterSelectorController.showPrinterSelector()
        updateSystemStatus()
    }
}