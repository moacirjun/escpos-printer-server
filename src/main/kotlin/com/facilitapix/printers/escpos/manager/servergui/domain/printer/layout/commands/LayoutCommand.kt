package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

fun interface LayoutCommand {
    fun execute(args: Map<String, Any>)
}