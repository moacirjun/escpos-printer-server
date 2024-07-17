package com.facilitapix.printers.escpos.manager.servergui

import com.dustinredmond.fxtrayicon.FXTrayIcon
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller.MainController
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller.PrinterSelectorController
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server.HttpServer
import io.github.palexdev.materialfx.theming.JavaFXThemes
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets
import io.github.palexdev.materialfx.theming.UserAgentBuilder
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.system.exitProcess
import javafx.application.Application as JavafxApplication

fun main(args: Array<String>) {
    JavafxApplication.launch(Application::class.java, *args)
}

class Application : JavafxApplication() {

    companion object {
        val scope = MainScope()

        var isDevModeEnabled: Boolean = false
            private set
    }

    override fun stop() {
        super.stop()
        scope.cancel()
    }

    override fun start(stage: Stage) {
        isDevModeEnabled = "--devMode=true" in parameters.raw

        configureAppTheme()
        configureFxTrayIcon(stage)

        initiateServer()
        configurePrinterConnection()

        if (isOnlyTrayIconModeEnabled()) {
            return
        }

        stage.apply {
            title = "Gerenciador de impressoras ESC/POS"
            scene = MainController.instantiateScene()
            show()
            sizeToScene()
        }

        if (PrinterConnector.getPersistedPrinter() == null) {
            Platform.runLater {
                PrinterSelectorController.showPrinterSelector()
            }
        }
    }

    private fun configurePrinterConnection() {
        if (PrinterConnector.getPersistedPrinter() != null) {
            connectToConfiguredPrinter()
        }
    }

    private fun isOnlyTrayIconModeEnabled() = "--onlyTrayIcon=true" in parameters.raw

    private fun configureAppTheme() {
        UserAgentBuilder.builder()
            .themes(JavaFXThemes.MODENA)
            .themes(MaterialFXStylesheets.forAssemble(true))
            .setDeploy(true)
            .setResolveAssets(true)
            .build()
            .setGlobal()
    }

    private fun configureFxTrayIcon(stage: Stage) {
        FXTrayIcon(stage, javaClass.getResource("facilita-pix-logo.png")).apply {
            show()
            setTrayIconTooltip("Facilita Pix Server Manager")
            addMenuItem(
                MenuItem("Abrir Gerenciador").apply {
                    styleClass.add("bold")
                    setOnAction {
                        if (stage.isIconified) {
                            stage.isIconified = false
                        }
                        stage.show()
                        stage.toFront()
                        stage.requestFocus()
                    }
                }
            )
            addSeparator()
            addMenuItem(
                MenuItem("Encerrar").apply {
                    setOnAction {
                        stage.close()
                        hide()
                        HttpServer.stop()
                        exitProcess(0)
                    }
                }
            )
        }
    }

    private fun connectToConfiguredPrinter() {
        try {
            PrinterConnector.connectToPersistedPrinter()
        } catch (e: Exception) {
            //TODO: remover esse modal. Deixar apenas o ícone de status da impressora com status de erro
            // e uma mensagem de error numa lable de descricao dealhada do status da impressora
//            Alert(Alert.AlertType.ERROR).apply {
//                title = "Erro ao conectar com a impressora"
//                headerText = "Erro ao conectar com a impressora"
//                contentText = "Não foi possível conectar com a impressora ${PrinterConnector.getPersistedPrinter()}. " +
//                        "Verifique se a impressora está ligada e conectada ao computador."
//                showAndWait()
//            }
        }
    }

    private fun initiateServer() {
        scope.launch(Dispatchers.IO) {
            HttpServer.start()
        }
    }
}

fun viewLoader(view: String) = FXMLLoader(Application::class.java.getResource(view))

fun <T> loadView(view: String): T = viewLoader(view).load()
