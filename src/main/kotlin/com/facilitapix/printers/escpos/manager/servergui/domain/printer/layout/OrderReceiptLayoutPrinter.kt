package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands.*
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceiptLayoutCommand
import com.github.anastaciocintra.escpos.EscPos

class OrderReceiptLayoutPrinter(
    private val printerContext: EscPos,
    private val order: Order,
) {
    enum class CommandName(val factory: LayoutCommandFactory) {
        WRITE_LF(WriteLfCommand.Factory),
        FEED(FeedCommand.Factory),
        QR_CODE(QrCodeCommand.Factory),
        CUT(CutCommand.Factory),
    }

    fun print(layoutCommands: List<OrderReceiptLayoutCommand>) {
        layoutCommands.forEach {
            instantiateCommand(it.name).execute(it.args)
        }
    }

    private fun instantiateCommand(commandName: String): LayoutCommand =
        CommandName.entries
            .find { it.name == commandName }
            ?.factory
            ?.create(printerContext, order)
        ?: throw IllegalArgumentException("Command not found: $commandName")
}