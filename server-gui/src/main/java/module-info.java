module com.facilitapix.printers.escpos.manager.servergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires transitive com.facilitapix.printers.escpos.server;
    requires transitive kotlinx.coroutines.core;
    requires transitive kotlinx.coroutines.javafx;

    opens com.facilitapix.printers.escpos.manager.servergui to javafx.fxml, kotlin.stdlib, kotlinx.coroutines.core;
    exports com.facilitapix.printers.escpos.manager.servergui;

    uses com.facilitapix.printers.escpos.server.HttpServer;
}