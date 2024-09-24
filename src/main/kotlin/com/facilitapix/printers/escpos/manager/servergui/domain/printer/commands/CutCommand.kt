package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos

class CutCommand(
    private val printerContext: EscPos,
) : PrinterCommand {

    override fun execute(args: Map<String, Any>) {
        printerContext.cut(
            resolveMode(resolveArgValue("mode", args))
        )
    }

    private fun resolveMode(mode: String): EscPos.CutMode {
        return EscPos.CutMode.entries.find { it.name.equals(mode, ignoreCase = true) }
            ?: EscPos.CutMode.FULL
    }

    object Factory : PrintCommandFactory {
        override fun create(printerContext: EscPos, orderReceipt: OrderReceipt): PrinterCommand =
            CutCommand(printerContext)
    }
}