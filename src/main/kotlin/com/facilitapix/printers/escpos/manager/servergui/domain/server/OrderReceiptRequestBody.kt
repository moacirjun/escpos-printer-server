package com.facilitapix.printers.escpos.manager.servergui.domain.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
data class OrderReceiptRequestBody(
    val order: Order,
    val layout: List<OrderReceiptLayoutCommand>,
)