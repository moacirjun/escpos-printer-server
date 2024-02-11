//package com.facilitapix
//
//import com.dustinredmond.fxtrayicon.FXTrayIcon
//import javafx.application.Application
//import javafx.event.ActionEvent
//import javafx.event.EventHandler
//import javafx.scene.Scene
//import javafx.scene.control.*
//import javafx.scene.layout.BorderPane
//import javafx.scene.layout.HBox
//import javafx.scene.layout.VBox
//import javafx.stage.Stage
//import java.net.URL
//
//class TestingJavaFX: Application() {
//
//    companion object {
//        /**
//         * Função main para iniciar a aplicação.
//         */
//        @JvmStatic
//        fun main(args: Array<String>) {
//            launch(TestingJavaFX::class.java)
//        }
//    }
//
//    override fun start(stage: Stage) {
//        val root = BorderPane()
//        stage.scene = Scene(root)
//
//        // By default, our FXTrayIcon will have an entry with our Application's title in bold font,
//        // when clicked, this MenuItem will call stage.show()
//        //
//        // This can be disabled by simply removing the MenuItem after instantiating the FXTrayIcon
//        // though, by convention, most applications implement this functionality.
//        stage.title = "FXTrayIcon test!"
//
//        // Instantiate the FXTrayIcon providing the parent Stage and a path to an Image file
//        val trayIcon = FXTrayIcon(stage, javaClass.getResource("/facilita-pix-logo.png"))
//        trayIcon.show()
//
//        // By default the FXTrayIcon's tooltip will be the parent stage's title, that we used in the constructor
//        // This method can override this
//        trayIcon.setTrayIconTooltip("An alternative tooltip!")
//
//        // We can now add JavaFX MenuItems to the menu
//        val menuItemTest = MenuItem("Create some JavaFX component!")
//        menuItemTest.onAction = EventHandler {
//            Alert(
//                Alert.AlertType.INFORMATION,
//                "We just ran some JavaFX code from an AWT MenuItem!"
//            ).showAndWait()
//        }
//        trayIcon.addMenuItem(menuItemTest)
//
//        // We can also nest menus, below is an Options menu with sub-items
//        val menuOptions = Menu("Options")
//        val miOn = MenuItem("On")
//        miOn.onAction = EventHandler {
//            println(
//                "Options -> On clicked"
//            )
//        }
//        val miOff = MenuItem("Off")
//        miOff.onAction = EventHandler {
//            println(
//                "Options -> Off clicked"
//            )
//        }
//        menuOptions.items.addAll(miOn, miOff)
//        trayIcon.addMenuItem(menuOptions)
//
//        val vBox = VBox(5.0)
//        vBox.children.add(
//            Label(
//                """
//            You should see a tray icon!
//            Try closing this window and double-clicking the icon.
//            Try single-clicking it.
//            """.trimIndent()
//            )
//        )
//        val buttonRemoveTrayIcon = Button("Remove TrayIcon")
//        vBox.children.add(buttonRemoveTrayIcon)
//
//        // Removing the FXTrayIcon, this will also cause the JVM to terminate
//        // after the last JavaFX Stage is hidden
//        buttonRemoveTrayIcon.onAction = EventHandler { trayIcon.hide() }
//
//        val buttonDefaultMsg = Button("Show a \"Default\" message")
//        // showDefaultMessage uses the FXTrayIcon image in the notification
//        buttonDefaultMsg.onAction = EventHandler {
//            trayIcon.showMessage(
//                "A caption text",
//                "Some content text."
//            )
//        }
//
//        val buttonInfoMsg = Button("Show a \"Info\" message")
//        // other showXXX methods use an icon appropriate for the message type
//        buttonInfoMsg.onAction = EventHandler {
//            trayIcon.showInfoMessage(
//                "A caption text",
//                "Some content text"
//            )
//        }
//
//        val buttonWarnMsg = Button("Show a \"Warn\" message")
//        buttonWarnMsg.onAction = EventHandler {
//            trayIcon.showWarningMessage(
//                "A caption text",
//                "Some content text"
//            )
//        }
//
//        val buttonErrorMsg = Button("Show a \"Error\" message")
//        buttonErrorMsg.onAction = EventHandler {
//            trayIcon.showErrorMessage(
//                "A caption text",
//                "Some content text"
//            )
//        }
//
//        val hBox = HBox(5.0, buttonDefaultMsg, buttonInfoMsg, buttonWarnMsg, buttonErrorMsg)
//        vBox.children.add(hBox)
//
//        root.center = vBox
//        stage.sizeToScene()
//        stage.show()
//    }
//
//    /**
//     * Test icon used for FXTrayIcon runnable tests
//     *
//     * @return URL to an example icon PNG
//     */
//    fun getIcon(): URL {
//        return javaClass.getResource("facilita-pix-logo.png")
//    }
//}