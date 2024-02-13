package com.facilitapix.printers.escpos.manager.servergui

interface HttpServer {
    fun start()
    fun stop()
    fun restart()
}