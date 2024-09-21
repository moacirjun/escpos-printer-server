package com.facilitapix.printers.escpos.manager.servergui.domain

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands.PrinterCommandBody
import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt

data class PrintOrderRequestBody(
    val orderReceipt: OrderReceipt,
    val commands: List<PrinterCommandBody>,
)