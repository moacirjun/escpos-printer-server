package com.facilitapix.printers.escpos.manager.servergui.domain.server

data class PrinterCommandRequestBody(
    val name: String,
    val args: Map<String, Any>,
)