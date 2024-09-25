package com.facilitapix.printers.escpos.manager.servergui.domain.printer

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceiptLayoutCommand
import com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.OrderReceiptLayoutPrinter
import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.escpos.barcode.QRCode
import org.slf4j.LoggerFactory

class PrinterService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAllPrinters(): List<String> = PrinterConnector.getAllPrinters()

    fun printOrderReceipt(order: Order) {
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
                writeLF(title, order.storeName ?: "VARGAS BIKE SHOP")

                feed(1)
                writeLF(body1, "Status: ${order.status}")
                feed(1)

                writeLF(body1, "Sequência: ${order.orderId}")
                writeLF(body2, order.customerName)
                writeLF(body1, order.amount)

                write(
                    QRCode().apply {
                        setSize(5)
                        setJustification(EscPosConst.Justification.Center)
                    },
                    order.qrCode
                )

                writeLF(body2, "Data: ${order.orderDate}")
                writeLF(secondaryText, "facilitapix.com")

                feed(1)
                feed(1)
                feed(1)
                cut(EscPos.CutMode.PART)
            }
        } catch (e: Exception) {
            logger.error("Error while printing order receipt. cause: ${e.message}", e)
            //TODO: handle error - Improve the return to the client
        }
    }

    fun printFromCommands(order: Order, layoutCommands: List<OrderReceiptLayoutCommand>): Result<Unit> =
        runCatching {
            PrinterConnector.connectAndExecuteCall {
                val orderReceiptLayoutPrinter = OrderReceiptLayoutPrinter(it, order)
                orderReceiptLayoutPrinter.print(layoutCommands)
            }
        }.onFailure {
            logger.error("Error while printing order receipt. cause: ${it.message}", it)
        }

    fun changeSelectedPrinter(printServiceName: String) {
        PrinterConnector.connectToPrinter(printServiceName)
        //TODO: handle error - Improve the return to the client
    }

    fun printTestReceipt() {
        try {
            printOrderReceipt(
                Order(
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