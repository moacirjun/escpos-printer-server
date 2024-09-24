package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands.utils

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt

fun <T> resolveArgValue(fieldName: String, args: Map<String, Any>): T =
    requireNotNull(resolveNullableArgValue(fieldName, args)) { "The field $fieldName is required. Received: $args" }

fun <T> resolveNullableArgValue(fieldName: String, args: Map<String, Any>): T? =
    try {
        args[fieldName] as? T
    } catch (exception: TypeCastException) {
        throw IllegalArgumentException("The field $fieldName has a unexpected Type.", exception)
    }

fun replaceOrderReceiptParams(orderReceipt: OrderReceipt, text: String): String =
    text
        .replace(OrderParamsSignature.ORDER_ID.signature, orderReceipt.orderId)
        .replace(OrderParamsSignature.STATUS.signature, orderReceipt.status)
        .replace(OrderParamsSignature.ORDER_DATE.signature, orderReceipt.orderDate)
        .replace(OrderParamsSignature.QR_CODE.signature, orderReceipt.qrCode)
        .replace(OrderParamsSignature.CUSTOMER_NAME.signature, orderReceipt.customerName)
        .replace(OrderParamsSignature.AMOUNT.signature, orderReceipt.amount)
        .replace(OrderParamsSignature.STORE_NAME.signature, orderReceipt.storeName ?: "")
