package com.facilitapix.printers.escpos.manager.servergui.domain.server

interface HttpServerInterface {
    fun start()
    fun stop()
    fun restart()

    fun isRunning(): Boolean
}