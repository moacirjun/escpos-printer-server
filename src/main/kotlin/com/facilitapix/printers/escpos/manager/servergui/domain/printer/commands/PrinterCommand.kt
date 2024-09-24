package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

fun interface PrinterCommand {
    fun execute(args: Map<String, Any>)
}