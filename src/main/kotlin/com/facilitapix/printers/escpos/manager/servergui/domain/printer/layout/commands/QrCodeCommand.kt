package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.replaceOrderReceiptParams
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.resolveNullableArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.barcode.QRCode

class QrCodeCommand(
    private val printerContext: EscPos,
    private val order: Order,
) : LayoutCommand {

    enum class ArgsNames(val fieldName: String) {
        SIZE("size"),
        JUSTIFICATION("justification"),
        DATA("data"),
    }

    override fun execute(args: Map<String, Any>) {
        printerContext.write(
            QRCode().apply {
                setSize(resolveNullableArgValue(ArgsNames.SIZE.fieldName, args) ?: 5)
                setJustification(
                    resolveJustification(resolveNullableArgValue(ArgsNames.JUSTIFICATION.fieldName, args))
                )
            },
            resolveQrCodeData(args)
        )
    }

    private fun resolveJustification(justification: String?): EscPosConst.Justification {
        if (justification == null) return EscPosConst.Justification.Center

        return EscPosConst.Justification.entries.find { it.name.equals(justification, ignoreCase = true) }
            ?: EscPosConst.Justification.Center
    }

    private fun resolveQrCodeData(args: Map<String, Any>): String =
        replaceOrderReceiptParams(order, resolveArgValue(ArgsNames.DATA.fieldName, args))

    object Factory : LayoutCommandFactory {
        override fun create(printerContext: EscPos, order: Order): LayoutCommand =
            QrCodeCommand(printerContext, order)
    }
}