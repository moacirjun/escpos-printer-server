package com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands

import com.facilitapix.printers.escpos.manager.servergui.domain.server.OrderReceipt
import com.github.anastaciocintra.escpos.EscPos
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class PrinterCommandsHandler(
    private val printerContext: EscPos,
    private val orderReceipt: OrderReceipt,
) {
    fun handleCommands(commands: List<PrinterCommandBody>) {
        commands.forEach {
            instantiateCommand(it.name).execute(it.args)
        }
    }

    private fun instantiateCommand(commandName: String): PrinterCommand {
        val commandClass = findCommandClassByName(commandName)
        val constructor = commandClass.primaryConstructor
        return constructor?.call(printerContext, orderReceipt) ?: throw IllegalArgumentException("No suitable constructor found for command: $commandName")
    }

    private fun findCommandClassByName(commandName: String): KClass<out PrinterCommand> {
        val packageName = "com.facilitapix.printers.escpos.manager.servergui.domain.printer.commands"
        val reflections = Reflections(packageName)
        val commandClasses = reflections.getSubTypesOf(PrinterCommand::class.java)

        return commandClasses.firstOrNull {
            val test = it.kotlin.objectInstance
            test?.let { obj ->
                obj.commandName == commandName
            } == true
        }?.kotlin ?: throw IllegalArgumentException("Command not found: $commandName")
    }
}