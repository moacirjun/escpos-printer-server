import com.facilitapix.printers.escpos.server.HttpServerImpl;

module com.facilitapix.printers.escpos.server {
    requires transitive kotlin.stdlib;
    requires transitive kotlinx.coroutines.core;
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
    requires transitive org.slf4j;
    requires transitive java.naming;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;

    exports com.facilitapix.printers.escpos.server;
    provides com.facilitapix.printers.escpos.server.HttpServer with HttpServerImpl;
}