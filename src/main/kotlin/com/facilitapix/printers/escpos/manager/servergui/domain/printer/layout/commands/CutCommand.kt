package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos

class CutCommand(
    private val printerContext: EscPos,
) : LayoutCommand {

    override fun execute(args: Map<String, Any>) {
        printerContext.cut(
            resolveMode(resolveArgValue("mode", args))
        )
    }

    private fun resolveMode(mode: String): EscPos.CutMode {
        return EscPos.CutMode.entries.find { it.name.equals(mode, ignoreCase = true) }
            ?: EscPos.CutMode.FULL
    }

    object Factory : LayoutCommandFactory {
        override fun create(printerContext: EscPos, order: Order): LayoutCommand =
            CutCommand(printerContext)
    }
}