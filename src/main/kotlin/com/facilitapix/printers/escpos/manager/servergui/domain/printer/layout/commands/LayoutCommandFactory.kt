package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos

fun interface LayoutCommandFactory {

    fun create(printerContext: EscPos, order: Order): LayoutCommand
}