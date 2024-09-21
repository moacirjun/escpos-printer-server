package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos

class PrinterCommandContext(
    val printer: EscPos,
    val orderReceipt: OrderReceipt,
)