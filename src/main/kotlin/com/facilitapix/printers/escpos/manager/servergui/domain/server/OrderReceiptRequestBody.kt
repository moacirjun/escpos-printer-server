package com.facilitapix.printers.escpos.manager.servergui.domain.server

data class OrderReceiptRequestBody(
    val order: Order,
    val layout: List<OrderReceiptLayoutCommand>,
)