package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

interface PrinterCommand {
    val commandName: String

    fun execute(args: Map<String, Any>)
}