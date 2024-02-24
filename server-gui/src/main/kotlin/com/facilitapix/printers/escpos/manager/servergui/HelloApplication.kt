package com.facilitapix.printers.escpos.manager.servergui

import com.dustinredmond.fxtrayicon.FXTrayIcon
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller.MainController
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller.PrinterSelectorController
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server.HttpServer
import io.github.palexdev.materialfx.theming.JavaFXThemes
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets
import io.github.palexdev.materialfx.theming.UserAgentBuilder
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.control.Alert
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    Application.launch(HelloApplication::class.java, *args)
}

class HelloApplication : Application() {

    companion object {
        val scope = MainScope()
    }

    override fun stop() {
        super.stop()
        scope.cancel()
    }

    override fun start(stage: Stage) {
        configureAppTheme()
        HttpServer.start()

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

        stage.apply {
            title = "Gerenciador de impressoras ESC/POS"
            scene = MainController.instantiateScene()
//            icons.add(javafx.scene.image.Image(javaClass.getResource("facilita-pix-logo.png")?.toExternalForm() ?: ""))

            show()
            sizeToScene()
        }

        if (hasPrinterConfigured()) {
            connectToConfiguredPrinter()
        } else {
            PrinterSelectorController.showPrinterSelector()
        }
    }

    private fun hasPrinterConfigured() = PrinterConnector.getPersistedPrinter() != null

    private fun connectToConfiguredPrinter() {
        try {
            PrinterConnector.connectToPersistedPrinter()
        } catch (e: Exception) {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Erro ao conectar com a impressora"
                headerText = "Erro ao conectar com a impressora"
                contentText = "Não foi possível conectar com a impressora ${PrinterConnector.getPersistedPrinter()}. " +
                        "Verifique se a impressora está ligada e conectada ao computador."
                showAndWait()
            }
        }
    }

    private fun configureAppTheme() {
        UserAgentBuilder.builder()
            .themes(JavaFXThemes.MODENA)
            .themes(MaterialFXStylesheets.forAssemble(true))
            .setDeploy(true)
            .setResolveAssets(true)
            .build()
            .setGlobal()
    }
}

fun viewLoader(view: String) = FXMLLoader(HelloApplication::class.java.getResource(view))

fun <T> loadView(view: String): T = viewLoader(view).load()
