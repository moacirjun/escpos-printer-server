package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.facilitapix.printers.escpos.manager.servergui.domain.server.PrinterCommandRequestBody
import com.github.anastaciocintra.escpos.EscPos

class PrinterCommandsHandler(
    private val printerContext: EscPos,
    private val orderReceipt: OrderReceipt,
) {
    enum class CommandName(val factory: PrintCommandFactory) {
        WRITE_LF(WriteLfCommand.Factory),
        FEED(FeedCommand.Factory),
        QR_CODE(QrCodeCommand.Factory),
        CUT(CutCommand.Factory),
    }

    fun handleCommands(commands: List<PrinterCommandRequestBody>) {
        commands.forEach {
            instantiateCommand(it.name).execute(it.args)
        }
    }

    private fun instantiateCommand(commandName: String): PrinterCommand =
        CommandName.entries
            .find { it.name == commandName }
            ?.factory
            ?.create(printerContext, orderReceipt)
        ?: throw IllegalArgumentException("Command not found: $commandName")
}