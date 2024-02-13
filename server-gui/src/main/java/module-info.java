module com.facilitapix.printers.escpos.manager.servergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires transitive com.facilitapix.printers.escpos.server;
    requires transitive kotlinx.coroutines.core;
    requires transitive kotlinx.coroutines.javafx;

    //slf4j and logback
    requires transitive org.slf4j;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;

    opens com.facilitapix.printers.escpos.manager.servergui to javafx.fxml, kotlin.stdlib, kotlinx.coroutines.core;
    exports com.facilitapix.printers.escpos.manager.servergui;

    uses com.facilitapix.printers.escpos.server.HttpServer;
    uses ch.qos.logback.classic.spi.Configurator;
}