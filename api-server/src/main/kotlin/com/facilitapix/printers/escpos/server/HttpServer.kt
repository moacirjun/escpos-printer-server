package com.facilitapix.printers.escpos.server

interface HttpServer {
    fun start()
    fun stop()
    fun restart()
}