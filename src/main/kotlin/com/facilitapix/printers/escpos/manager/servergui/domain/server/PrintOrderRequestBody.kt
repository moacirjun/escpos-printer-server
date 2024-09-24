package com.facilitapix.printers.escpos.manager.servergui.domain.server

data class PrintOrderRequestBody(
    val orderReceipt: OrderReceipt,
    val commands: List<PrinterCommandRequestBody>,
)