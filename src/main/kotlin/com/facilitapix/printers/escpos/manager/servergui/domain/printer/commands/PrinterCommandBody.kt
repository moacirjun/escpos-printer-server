package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

data class PrinterCommandBody(
    val name: String,
    val args: Map<String, Any>,
)