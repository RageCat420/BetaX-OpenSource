package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Formatting
import java.awt.Color


object Durability: DragModule("Durability","耐久",Category.HUD,"?",400,450,100,100) {


    override fun renderDragger(context: DrawContext) {
        test()
        drag()
        if (!mc.player!!.mainHandStack.isEmpty && mc.player!!.mainHandStack.isDamageable) {
            val stack = mc.player!!.mainHandStack
            val content = (stack.maxDamage - stack.damage).toString()
            val g = (stack.maxDamage - stack.damage.toFloat()) / stack.maxDamage
            val r = 1.0f - g
            val durability = "${Formatting.GRAY}Durability "
            context.drawTextWithShadow(mc.textRenderer, durability, x.toInt(), y, -1)
            context.drawTextWithShadow(
                mc.textRenderer,
                content,
                x - mc.textRenderer.getWidth(content) - 2,
                y,
                Color(r, g, 0f).rgb
            )
        }
        drawDescription(context, mouseX, mouseY, description)
        drawRectWhite(context, mouseX, mouseY)
    }


}
