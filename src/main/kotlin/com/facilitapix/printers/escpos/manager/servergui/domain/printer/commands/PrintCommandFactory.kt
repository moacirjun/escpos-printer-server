package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos

fun interface PrintCommandFactory {

    fun create(printerContext: EscPos, orderReceipt: OrderReceipt): PrinterCommand
}