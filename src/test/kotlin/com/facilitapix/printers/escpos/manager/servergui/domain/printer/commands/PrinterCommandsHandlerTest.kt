package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.Style
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PrinterCommandsHandlerTest {

    private lateinit var printerCommandsHandler: PrinterCommandsHandler

    @Mock
    private lateinit var printerContext: EscPos

    private lateinit var orderReceipt: OrderReceipt

    @BeforeEach
    fun setUp() {
        orderReceipt = OrderReceipt(
            orderId = "123",
            customerName = "John Doe",
            amount = "R$ 100,00",
            qrCode = "https://vargasbike.com.br",
            orderDate = "2021-10-10",
            status = "PENDING",
            storeName = "Vargas Bike Shop",
        )

        printerCommandsHandler = PrinterCommandsHandler(printerContext, orderReceipt)
    }

    @Test
    fun `should instantiate command and execute it`() {
        val commands = listOf(
            PrinterCommandBody(
                name = "WRITE_LF",
                args = mapOf(
                    "style" to "TITLE",
                    "text" to "VARGAS BIKE SHOP",
                )
            )
        )

        printerCommandsHandler.handleCommands(commands)

        // verify
        verify(printerContext).writeLF(any<Style>(), any())
    }
}