package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterService
import com.facilitapix.printers.escpos.manager.servergui.loadView
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class PrinterSelectorController {

    @FXML
    private lateinit var printerListView: ListView<String>

    @FXML
    private lateinit var selectPrinterBtn: Button

    private val printerService = PrinterService()

    @FXML
    private fun initialize() {
        val currentPrinter = PrinterConnector.getSelectedPrinter()
        val printers = printerService.getAllPrinters()
        if (printers.isEmpty()) {
            showAlert(
                "Nenhuma Impressora Encontrada",
                "Não foi encontrada nenhuma impressora conectada ao sistema. " +
                        "Verifique se a impressora está conectada e ligada."
            )
        }

        printerListView.items.addAll(printers)
        printerListView.selectionModel.select(currentPrinter)
        printerListView.setCellFactory { _ ->
            object : ListCell<String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty || item == null) {
                        text = null
                        return
                    }

                    text = item
                    style = if (item == currentPrinter) "-fx-font-weight: bold; -fx-background-color: #f0f0f0;" else ""
                }
            }
        }

        selectPrinterBtn.isDisable = true
        printerListView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            selectPrinterBtn.isDisable = newValue == null || newValue == currentPrinter
            selectPrinterBtn.tooltip = if (selectPrinterBtn.isDisable) {
                Tooltip("A impressora selecionada já está configurada como atual.")
            } else {
                Tooltip("Selecione uma impressora para configurá-la como atual.")
            }
        }
    }

    @FXML
    private fun handleSelectPrinterBtnClick() {
        try {
            printerService.changeSelectedPrinter(printerListView.selectionModel.selectedItem)
            (printerListView.scene.window as Stage).close()
//        } catch (e: SpecificExceptionType) {
//            logger.error("Erro específico ao trocar de impressora", e)
//            showAlert("Erro ao Trocar Impressora", e.message ?: "Erro específico ao trocar de impressora.")
        } catch (e: Exception) {
            logger.error("Erro ao trocar de impressora", e)
            showAlert("Erro ao Trocar Impressora", "Não foi possível trocar a impressora. Tente novamente.")
        }
    }

    @FXML
    private fun handleCancelSelectPrinterBtnClick() {
        (printerListView.scene.window as Stage).close()
    }

    private fun showAlert(title: String, content: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.headerText = null
        alert.contentText = content
        alert.showAndWait()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PrinterSelectorController::class.java)

        fun showPrinterSelector() {
            Stage().apply {
                scene = Scene(instantiateView())
                initModality(Modality.APPLICATION_MODAL)
                showAndWait()
            }
        }

        fun instantiateView() = loadView<VBox>("printer-selector.fxml")
    }
}