package com.facilitapix.printers.escpos.manager.servergui

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.PrinterConnector
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller.PrinterSelectorController
import com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server.HttpServer
import io.github.palexdev.materialfx.theming.JavaFXThemes
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets
import io.github.palexdev.materialfx.theming.UserAgentBuilder
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle


fun main(args: Array<String>) {
    Application.launch(HelloApplication::class.java, *args)
}

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        UserAgentBuilder.builder()
            .themes(JavaFXThemes.MODENA)
            .themes(MaterialFXStylesheets.forAssemble(true))
            .setDeploy(true)
            .setResolveAssets(true)
            .build()
            .setGlobal()

        val mainPage = loadView<VBox>("main-view.fxml")

        stage.apply {
            title = "Gerenciador de impressoras ESC/POS"
            initStyle(StageStyle.UTILITY)
            scene = Scene(mainPage).apply {
                fill = Color.TRANSPARENT
            }

            show()
            sizeToScene()
        }
    }
}

fun viewLoader(view: String) = FXMLLoader(HelloApplication::class.java.getResource(view))

fun <T> loadView(view: String): T = viewLoader(view).load()
