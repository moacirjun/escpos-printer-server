module com.facilitapix.printers.escpos.manager.servergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires kotlin.stdlib;
    requires transitive kotlinx.coroutines.core;
    requires transitive kotlinx.coroutines.javafx;

    //slf4j and logback
    requires transitive org.slf4j;
    requires transitive java.naming;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;

    //Server and printer libs
    requires io.ktor.server.host.common;
    requires io.ktor.server.netty;
    requires io.ktor.server.core;
    requires io.ktor.server.content.negotiation;
    requires io.ktor.serialization.jackson;
    requires com.fasterxml.jackson.databind;
    requires io.ktor.server.cors;
    requires io.ktor.http;
    requires java.desktop;
    requires escpos.coffee;

    opens com.facilitapix.printers.escpos.manager.servergui to javafx.fxml, kotlin.stdlib, kotlinx.coroutines.core;
    opens com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller to javafx.fxml, kotlin.stdlib, kotlinx.coroutines.core;
    exports com.facilitapix.printers.escpos.manager.servergui;
    exports com.facilitapix.printers.escpos.manager.servergui.infrasctructure.server;
    exports com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller;

    uses ch.qos.logback.classic.spi.Configurator;
}