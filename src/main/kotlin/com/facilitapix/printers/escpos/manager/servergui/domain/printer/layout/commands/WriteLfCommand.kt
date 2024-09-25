package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.resolveArgValue
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils.replaceOrderReceiptParams
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.Style

class WriteLfCommand(
    private val printerContext: EscPos,
    private val order: Order,
) : LayoutCommand {

    private val title = Style().apply {
        setFontSize(Style.FontSize._2, Style.FontSize._2)
        setJustification(EscPosConst.Justification.Center)
        setBold(true)
    }

    private val body1 = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
        setBold(true)
    }

    private val body2 = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
    }

    private val secondaryText = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
        setUnderline(Style.Underline.OneDotThick)
    }

    enum class Styles {
        TITLE,
        BODY1,
        BODY2,
        SECONDARY;

        companion object {
            fun fromString(styleName: String): Styles =
                entries.find { it.name.equals(styleName, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Style not found: $styleName")
        }
    }

    enum class ArgsNames(val fieldName: String) {
        STYLE("style"),
        TEXT("text"),
    }

    override fun execute(args: Map<String, Any>) {
        printerContext.writeLF(
            resolveStyle(args),
            resolveText(args),
        )
    }

    private fun resolveStyle(args: Map<String, Any>): Style {
        val styleName = resolveArgValue<String>(ArgsNames.STYLE.fieldName, args)
        val style = Styles.fromString(styleName)
        return when (style) {
            Styles.TITLE -> title
            Styles.BODY1 -> body1
            Styles.BODY2 -> body2
            Styles.SECONDARY -> secondaryText
        }
    }

    private fun resolveText(args: Map<String, Any>): String {
        val rawText = resolveArgValue<String>(ArgsNames.TEXT.fieldName, args)
        return replaceOrderReceiptParams(order, rawText)
    }

    object Factory : LayoutCommandFactory {
        override fun create(printerContext: EscPos, order: Order): LayoutCommand =
            WriteLfCommand(printerContext, order)
    }
}