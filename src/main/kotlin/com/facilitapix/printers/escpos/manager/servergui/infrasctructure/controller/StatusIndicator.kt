package com.facilitapix.printers.escpos.manager.servergui.infrasctructure.controller

import javafx.animation.FillTransition
import javafx.animation.Interpolator
import javafx.animation.ScaleTransition
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration

class StatusIndicator(private val circle: Circle, private val pulseCircle: Circle) {
    private var fillTransition: FillTransition
    private var scaleTransition: ScaleTransition = ScaleTransition(ANIMATION_DURATION, pulseCircle).apply {
        byX = 1.1
        byY = 1.1
        cycleCount = ScaleTransition.INDEFINITE
        interpolator = Interpolator.EASE_OUT
    }

    init {
        fillTransition = createFillTransition(SUCCESS_COLOR_40, SUCCESS_COLOR_10)
    }

    private fun createFillTransition(fromColor: Color, toColor: Color): FillTransition =
        FillTransition(ANIMATION_DURATION, pulseCircle).apply {
            fromValue = fromColor
            toValue = toColor
            cycleCount = FillTransition.INDEFINITE
            interpolator = Interpolator.EASE_OUT
        }

    fun startAnimation() {
        pulseCircle.scaleX = 1.0
        pulseCircle.scaleY = 1.0
        scaleTransition.play()
        fillTransition.play()
    }

    fun stopAnimation() {
        scaleTransition.stop()
        pulseCircle.scaleX = 1.0
        pulseCircle.scaleY = 1.0
        fillTransition.stop()
    }

    fun error() {
        stopAnimation()
        circle.fill = ERROR_COLOR
        fillTransition = createFillTransition(ERROR_COLOR_40, ERROR_COLOR_10)
        startAnimation()
    }

    fun success() {
        stopAnimation()
        circle.fill = SUCCESS_COLOR
        fillTransition = createFillTransition(SUCCESS_COLOR_40, SUCCESS_COLOR_10)
        startAnimation()
    }

    fun hide() {
        stopAnimation()
        circle.fill = Color.TRANSPARENT
        pulseCircle.fill = Color.TRANSPARENT
    }

    companion object {
        private val SUCCESS_COLOR = Color.rgb(60, 179, 113)
        private val SUCCESS_COLOR_40 = Color.rgb(60, 179, 113, 0.4)
        private val SUCCESS_COLOR_10 = Color.rgb(60, 179, 113, 0.01)
        private val ERROR_COLOR = Color.rgb(255, 0, 0)
        private val ERROR_COLOR_40 = Color.rgb(255, 0, 0, 0.4)
        private val ERROR_COLOR_10 = Color.rgb(255, 0, 0, 0.01)
        private val ANIMATION_DURATION = Duration.seconds(1.5)
    }
}