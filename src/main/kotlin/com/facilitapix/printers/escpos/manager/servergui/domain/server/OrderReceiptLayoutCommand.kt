package com.facilitapix.printers.escpos.manager.servergui.domain.server

data class OrderReceiptLayoutCommand(
    val name: String,
    val args: Map<String, Any>,
)