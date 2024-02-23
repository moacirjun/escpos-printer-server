package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.escpos.barcode.QRCode
import org.slf4j.LoggerFactory

class PrinterService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAllPrinters(): List<String> = PrinterConnector.getAllPrinters()

    fun printOrderReceipt(orderReceipt: OrderReceipt) {
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

        try {
            PrinterConnector.connectAndRunCommands {
                writeLF(title, "VARGAS BIKE SHOP")

                feed(1)
                writeLF(body1, "Status: ${orderReceipt.status}")
                feed(1)

                writeLF(body1, "Sequência: ${orderReceipt.orderId}")
                writeLF(body2, orderReceipt.customerName)
                writeLF(body1, orderReceipt.amount)

                write(
                    QRCode().apply {
                        setSize(5)
                        setJustification(EscPosConst.Justification.Center)
                    },
                    orderReceipt.qrCode
                )

                writeLF(body2, "Data: ${orderReceipt.orderDate}")
                writeLF(secondaryText, "facilitapix.com")

                feed(1)
                cut(EscPos.CutMode.PART)
            }
        } catch (e: Exception) {
            logger.error("Error while printing order receipt. cause: ${e.message}", e)
            //TODO: handle error - Improve the return to the client
        }
    }

    fun changeSelectedPrinter(printServiceName: String) {
        PrinterConnector.connectToPrinter(printServiceName)
        //TODO: handle error - Improve the return to the client
    }

    fun printTestReceipt() {
        try {
            printOrderReceipt(
                OrderReceipt(
                    orderId = "123456",
                    customerName = "João da Silva",
                    amount = "R$ 123,45",
                    qrCode = "https://facilitapix.com",
                    orderDate = "01/01/2021",
                    status = "Aguardando pagamento"
                )
            )
        } catch (e: Exception) {
            logger.error("Error while printing test receipt. cause: ${e.message}", e)
            throw e
        }
    }
}