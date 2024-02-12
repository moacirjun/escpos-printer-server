package com.facilitapix.printers.escpos.server.domain

import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.escpos.barcode.QRCode
import com.github.anastaciocintra.output.PrinterOutputStream

fun getAllPrinters(): List<String> = PrinterOutputStream.getListPrintServicesNames().toList()

fun printOrderReceipt(orderReceipt: OrderReceipt) {
    val qrcode = QRCode().apply {
        setSize(5)
        setJustification(EscPosConst.Justification.Center)
    }

    val title = Style().apply {
        setFontSize(Style.FontSize._2, Style.FontSize._2)
        setJustification(EscPosConst.Justification.Center)
        setBold(true)
    }

    val body1 = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
        setBold(true)
    }

    val body2 = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
    }

    val secondaryText = Style().apply {
        setFontSize(Style.FontSize._1, Style.FontSize._1)
        setJustification(EscPosConst.Justification.Center)
        setUnderline(Style.Underline.OneDotThick)
    }

    PrinterConnector.connectAndRunCommands {
        writeLF(title, "VARGAS BIKE SHOP")

        feed(1)
        writeLF(body1, "Status: ${orderReceipt.status}")
        feed(1)

        writeLF(body1, "Sequência: ${orderReceipt.orderId}")
        writeLF(body2, orderReceipt.customerName)
        writeLF(body1, orderReceipt.amount)

        write(qrcode, orderReceipt.qrCode)

        writeLF(body2, "Data: ${orderReceipt.orderDate}")
        writeLF(secondaryText, "facilitapix.com")

        feed(1)
        cut(EscPos.CutMode.PART)
    }
}