package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos

class FeedCommand(private val printerContext: EscPos) : PrinterCommand {

    override fun execute(args: Map<String, Any>) {
        printerContext.feed(
            resolveArgValue("lines", args)
        )
    }

    object Factory : PrintCommandFactory {
        override fun create(printerContext: EscPos, orderReceipt: OrderReceipt): PrinterCommand =
            FeedCommand(printerContext)
    }
}