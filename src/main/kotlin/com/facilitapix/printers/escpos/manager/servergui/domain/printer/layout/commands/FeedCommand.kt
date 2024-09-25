package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos

class FeedCommand(private val printerContext: EscPos) : LayoutCommand {

    override fun execute(args: Map<String, Any>) {
        printerContext.feed(
            resolveArgValue("lines", args)
        )
    }

    object Factory : LayoutCommandFactory {
        override fun create(printerContext: EscPos, order: Order): LayoutCommand =
            FeedCommand(printerContext)
    }
}