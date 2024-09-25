package com.facilitapix.printers.escpos.manager.servergui.domain.printer.layout.utils

import com.facilitapix.printers.escpos.manager.servergui.domain.server.Order

fun <T> resolveArgValue(fieldName: String, args: Map<String, Any>): T =
    requireNotNull(resolveNullableArgValue(fieldName, args)) { "The field $fieldName is required. Received: $args" }

fun <T> resolveNullableArgValue(fieldName: String, args: Map<String, Any>): T? =
    try {
        args[fieldName] as? T
    } catch (exception: TypeCastException) {
        throw IllegalArgumentException("The field $fieldName has a unexpected Type.", exception)
    }

fun replaceOrderReceiptParams(order: Order, text: String): String =
    text
        .replace(OrderParamsSignature.ORDER_ID.signature, order.orderId)
        .replace(OrderParamsSignature.STATUS.signature, order.status)
        .replace(OrderParamsSignature.ORDER_DATE.signature, order.orderDate)
        .replace(OrderParamsSignature.QR_CODE.signature, order.qrCode)
        .replace(OrderParamsSignature.CUSTOMER_NAME.signature, order.customerName)
        .replace(OrderParamsSignature.AMOUNT.signature, order.amount)
        .replace(OrderParamsSignature.STORE_NAME.signature, order.storeName ?: "")
