package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller

import com.facilitapix.printers.escpos.manager.servergui.HelloApplication
import com.facilitapix.printers.escpos.manager.servergui.domain.SystemStatus
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterService
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server.HttpServer
import com.facilitapix.printers.escpos.manager.servergui.loadView
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


/*
TODO:
- [ ] Splash screen enquanto sobe o servidor e conecta com a impressora
- [ ] Tray icon dinâmico de acordo com o status do servidor e da impressora
 */
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

    private val printerService = PrinterService()
    private val currentSelectedPrinter
        get() = PrinterConnector.getSelectedPrinter()

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
        if (currentSelectedPrinter != null) {
            connectToConfiguredPrinter()
        } else {
            PrinterSelectorController.showPrinterSelector()
        }

        selectedPrinterLbl.textProperty().bind(systemStatus.selectedPrinter)
        serverStatusLbl.textProperty().bind(systemStatus.serverStatus)

        printerStatusIndicator.startAnimation()
        serverStatusIndicator.startAnimation()

        updatePrinterStatus()
        updateServerStatus()
    }

    private fun connectToConfiguredPrinter() {
        try {
            PrinterConnector.connectToPersistedPrinter()
        } catch (e: Exception) {
            //TODO: remover esse modal. Deixar apenas o ícone de status da impressora com status de erro
            // e uma mensagem de error numa lable de descricao dealhada do status da impressora
            Alert(Alert.AlertType.ERROR).apply {
                title = "Erro ao conectar com a impressora"
                headerText = "Erro ao conectar com a impressora"
                contentText = "Não foi possível conectar com a impressora ${PrinterConnector.getPersistedPrinter()}. " +
                        "Verifique se a impressora está ligada e conectada ao computador."
                showAndWait()
            }
        }
    }

    private fun updateServerStatus() {
        HelloApplication.scope.launch(Dispatchers.Main) {
            fetchServerStatus.isDisable = true
            systemStatus.serverStatus.value = "Buscando status do servidor..."
            serverStatusIndicator.hide()

            val serverRunning = withContext(Dispatchers.IO) {
                isServerRunning()
            }

            fetchServerStatus.isDisable = false
            restartServerBtn.isDisable = false
            if (serverRunning) {
                systemStatus.serverStatus.value = SERVER_RUNNING_MESSAGE
                serverStatusIndicator.success()
            } else {
                systemStatus.serverStatus.value = SERVER_NOT_RUNNING_MESSAGE
                serverStatusIndicator.error()
            }
        }
    }

    private fun updatePrinterStatus() {
        systemStatus.selectedPrinter.value = currentSelectedPrinter ?: NO_PRINTER_CONFIGURED_MESSAGE
        if (systemStatus.selectedPrinter.value != NO_PRINTER_CONFIGURED_MESSAGE) {
            printerStatusIndicator.success()
            changeSelectedPrinterBtn.text = "Alterar"
            printExampleReceiptBtn.isVisible = true
            printExampleReceiptBtn.isDisable = false
        } else {
            systemStatus.selectedPrinter.value = NO_PRINTER_CONFIGURED_MESSAGE
            printerStatusIndicator.error()
            changeSelectedPrinterBtn.text = "Selecionar"
            printExampleReceiptBtn.isVisible = false
        }
    }

    private fun isServerRunning(): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://localhost:8087")
            .build()

        return try {
            // Executa a requisição e captura a resposta
            val response: Response = client.newCall(request).execute()
            response.isSuccessful // Retorna true se o status for 200-299
        } catch (e: Exception) {
            false // Se houver uma exceção, assume que o servidor não está rodando
        } finally {
            client.dispatcher.executorService.shutdown() // Encerra o cliente OkHttp
        }
    }

    @FXML
    private fun handleChangeSelectedPrinterBtnClick() {
        PrinterSelectorController.showPrinterSelector()
        updatePrinterStatus()
    }

    @FXML
    private fun handlePrintExampleReceiptBtnClick() {
        printExampleReceiptBtn.isDisable = true
        systemStatus.selectedPrinter.value = "Imprimindo recibo de exemplo..."

        HelloApplication.scope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    printerService.printTestReceipt()
                }
            } catch (e: Exception) {
                Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao imprimir recibo de exemplo. Verifique a conexão com a impressora e tente novamente."
                ).showAndWait()
            } finally {
                updatePrinterStatus()
            }
        }
    }

    @FXML
    private fun handleRestartServerBtnClick() {
        restartServerBtn.isDisable = true
        fetchServerStatus.isDisable = true
        systemStatus.serverStatus.value = "Reiniciando servidor..."
        serverStatusIndicator.hide()

        HelloApplication.scope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    HttpServer.restart()
                }
            } catch (e: Exception) {
                Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao reiniciar servidor. Verifique a conexão com a internet e tente novamente."
                ).showAndWait()
            } finally {
                updateServerStatus()
            }
        }
    }

    @FXML
    private fun handleFetchServerStatusBtnClick() {
        updateServerStatus()
    }
}