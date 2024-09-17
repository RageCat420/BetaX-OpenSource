package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import cn.trollaura.betax.systems.modules.client.Component
import cn.trollaura.betax.util.ColorUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.util.Formatting
import kotlin.math.abs
import kotlin.math.sqrt

object Speed: DragModule("Speed","速度显示",Category.HUD,"?",
    150,300,50,10) {


    override fun renderDragger(context: DrawContext) {
        test()
        drag()
        val speed: Double = getSpeed()

        if (mc.currentScreen is ChatScreen && y > (mc.textRenderer.fontHeight - 1 - 3 )) y -= 15

        val text = Formatting.GRAY.toString() + "Speed " + getString(speed) + "km/h"

        context.drawTextWithShadow(mc.textRenderer, text, x, y, if(Component.rainbow.get()) ColorUtil.rainbow(100).rgb else -1)
        drawDescription(context,mouseX,mouseY,description)
        drawRectWhite(context,mouseX,mouseY)
    }

    private fun getSpeed(): Double {
        if (mc.player == null) return 0.0
        val tX = abs(mc.player!!.x - mc.player!!.prevX)
        val tZ = abs(mc.player!!.z - mc.player!!.prevZ)
        val length = sqrt(tX * tX + tZ * tZ)

        return length * 20 * 3.6
    }

    private fun getString(pos: Double): String {
        return String.format("%.1f", pos).replace(',', '.')
    }


}
