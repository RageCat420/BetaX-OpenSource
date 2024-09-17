package cn.trollaura.betax.util

import java.awt.Color
import kotlin.math.ceil

object ColorUtil {

    fun rainbow(delay: Int): Color {
        var rainbowState = ceil((System.currentTimeMillis() + delay.toLong()).toDouble() / 20.0)
        return Color.getHSBColor((360.0.also { rainbowState %= it } / 360.0).toFloat(),
             255f,
            255f)
    }
}
