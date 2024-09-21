package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands.utils

enum class OrderParamsSignature(val signature: String) {
    STORE_NAME("{{store_name}}"),
    ORDER_ID("{{order_id}}"),
    STATUS("{{status}}"),
    ORDER_DATE("{{order_date}}"),
    QR_CODE("{{qr_code}}"),
    CUSTOMER_NAME("{{customer_name}}"),
    AMOUNT("{{amount}}"),
}