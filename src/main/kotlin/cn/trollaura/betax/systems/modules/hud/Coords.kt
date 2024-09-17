package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import cn.trollaura.betax.systems.modules.client.Component
import cn.trollaura.betax.util.ColorUtil
import dev.niuren.ic.Setting
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper

object Coords: DragModule("Coords","坐标",Category.HUD,"?",150,200,100,100) {

    val netherCoords = Setting("NetherCoords",this,true)
    val direction = Setting("Direction",this,true)

    override fun renderDragger(context: DrawContext) {
        test()
        drag()
        try {
            context.drawTextWithShadow(
                mc.textRenderer,
                Formatting.GRAY.toString() + "XYZ " + Formatting.WHITE +
                    getString(mc.player!!.x) +
                    Formatting.GRAY + ", " + Formatting.WHITE +
                    getString(mc.player!!.y) +
                    Formatting.GRAY + ", " + Formatting.WHITE +
                    getString(mc.player!!.z) +
                    if (netherCoords.get()) Formatting.GRAY.toString() + " [" + Formatting.WHITE +
                        getString(worldCords(mc.player!!.x)) +
                        Formatting.GRAY + ", " + Formatting.WHITE +
                        getString(worldCords(mc.player!!.z)) + Formatting.GRAY + "]" else "",
                x,
                y,
                if(Component.rainbow.get()) ColorUtil.rainbow(100).rgb else -1
            )
        } catch (e: IllegalStateException) {
            e.fillInStackTrace()
        }
        if(this.direction.get()) {
            context.drawTextWithShadow(
                mc.textRenderer,
                getFacing() + Formatting.GRAY + " [" + Formatting.WHITE + getTowards() + Formatting.GRAY + "]",
                x,
                y - mc.textRenderer.fontHeight,
                if(Component.rainbow.get()) ColorUtil.rainbow(100).rgb else -1
            )
        }
        drawDescription(context,mouseX,mouseY,description)
        drawRectWhite(context,mouseX,mouseY)
    }

    private fun worldCords(cords: Double): Double {
        return if (mc.world!!.dimension.respawnAnchorWorks()) cords * 8 else cords / 8
    }

    private fun getString(pos: Double): String {
        return String.format("%.1f", pos).replace(',', '.')
    }
    private fun getTowards(): String {
        return when (MathHelper.floor((mc.player!!.yaw * 4.0f / 360.0f).toDouble() + 0.5) and 3) {
            0 -> "+Z"
            1 -> "-X"
            2 -> "-Z"
            3 -> "+X"
            else -> "Invalid"
        }
    }
    private fun getFacing(): String {
        return when (MathHelper.floor((mc.player!!.yaw * 4.0f / 360.0f).toDouble() + 0.5) and 3) {
            0 -> "South"
            1 -> "West"
            2 -> "North"
            3 -> "East"
            else -> "Invalid"
        }
    }

}
