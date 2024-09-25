package com.facilitapix.printers.escpos.manager.servergui.domain.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties
data class Order(
    @JsonProperty("order_id")
    val orderId: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("order_date")
    val orderDate: String,
    @JsonProperty("qr_code")
    val qrCode: String,
    @JsonProperty("customer_name")
    val customerName: String,
    @JsonProperty("amount")
    val amount: String,
    @JsonProperty("store_name")
    val storeName: String? = null,
)

/* JSON EXAMPLE
{
  "order_id": "123456",
  "status": "Pendente",
  "order_date": "2021-10-01",
  "qr_code": "https://facilitapix.com/qr/123456",
  "customer_name": "João da Silva",
  "amount": "R$ 123,45"
}
 */