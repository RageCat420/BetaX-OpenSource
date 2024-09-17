package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import dev.niuren.utils.render.Render2DUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

object Armor: DragModule("Armor","装备显示",Category.HUD,"?",
    400,300,50,15) {



    override fun renderDragger(context: DrawContext) {
        test()
        drag()
        var armorX: Double
        var armorY: Double
        var slot = 3

        for (position in 0..3) {
            val itemStack = mc.player!!.inventory.getArmorStack(slot)
            armorX = (x + position * 18).toDouble()
            armorY = y.toDouble()
            Render2DUtils.drawItem(context, itemStack, armorX.toInt(), armorY.toInt(), 1f, true)
            if (itemStack.isDamageable) {
                val g = (itemStack.maxDamage - itemStack.damage.toFloat()) / itemStack.maxDamage
                val r = 1.0f - g
                val text = Math.round((itemStack.maxDamage - itemStack.damage) * 100f / itemStack.maxDamage.toFloat())
                    .toString()
                context.drawTextWithShadow(
                    mc.textRenderer,
                    text,
                    armorX.toInt() + 1,
                    armorY.toInt() - 10,
                    Color(r, g, 0f).rgb
                )
            }
            slot--
        }
        drawDescription(context,mouseX,mouseY,description)
        drawRectWhite(context,mouseX,mouseY)
    }



}
