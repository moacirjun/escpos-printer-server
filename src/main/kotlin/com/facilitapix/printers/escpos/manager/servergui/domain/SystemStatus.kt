package com.facilitapix.printers.escpos.manager.servergui.domain

import javafx.beans.property.SimpleStringProperty

data class SystemStatus(
    val serverStatus: SimpleStringProperty,
    val selectedPrinter: SimpleStringProperty,
)