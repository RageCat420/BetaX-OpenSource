package cn.trollaura.betax.systems.modules

import cn.trollaura.betax.gui.HUDGui
import cn.trollaura.betax.gui.HUDGui.height
import cn.trollaura.betax.gui.HUDGui.width
import cn.trollaura.betax.gui.drag.Dragger
import cn.trollaura.betax.systems.modules.client.Component
import com.google.common.annotations.Beta
import dev.niuren.BetaX
import dev.niuren.gui.font.FontRenderers
import dev.niuren.systems.modules.Module
import dev.niuren.systems.modules.client.ClickGUI
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Colors
import java.awt.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

abstract class DragModule( val name_: String, val name2_: String, val category_: Category, val description_: String, var x: Int, var y: Int,var width: Int, var height: Int): Module(name_,name2_,category_,description_),Dragger {
    override var dragging: Boolean = false
    override var dragX: Int = 0
    override var dragY: Int = 0
    var mouseX: Int = 0
    var mouseY: Int = 0

    abstract override fun renderDragger(context: DrawContext)

    override fun mouseUp(mouseX: Int, mouseY: Int) {
        dragging = false
    }

    fun test() {
        if(mc.currentScreen !is HUDGui) this.dragging = false
    }

    fun drag() {
        if (dragging) {
            x = animate(x.toDouble(),dragX + mouseX.toDouble()).toInt()
            y = animate(y.toDouble(),dragY + mouseY.toDouble()).toInt()
        }
    }

    fun animate(current: Double, endPoint: Double): Double {
        return animate(current, endPoint, Component.animationSpeed.get().toDouble() / 10)
    }

    fun animate(current: Double, endPoint: Double, speed: Double): Double {
        val shouldContinueAnimation = endPoint > current
        val dif = max(endPoint, current) - min(endPoint, current)
        val factor = dif * speed
        return if (abs(factor) <= 0.001) endPoint else current + if (shouldContinueAnimation) factor else -factor
    }

    override fun mouseDown(mouseX: Int, mouseY: Int, button: Int) {
        if(hover(x,y,width,height,mouseX,mouseY)) {

            if(button == 0) {
                dragging = true
                dragX = x - mouseX
                dragY = y - mouseY
            }
        }
    }

    override fun drawDescription(context: DrawContext, mouseX: Int, mouseY: Int, description: String) {
        if (!hover(x,y,width,height,mouseX,mouseY)) return


        val x = BetaX.mc.window.scaledWidth - BetaX.mc.textRenderer.getWidth(description)
        val y = BetaX.mc.window.scaledHeight - 10

        context.fill(
            x - 2,
            y - 2,
            (x + BetaX.mc.textRenderer.getWidth(description) + 1 * 1.5).toInt(),
            y + BetaX.mc.textRenderer.fontHeight + 2,
            Color(50, 50, 50).rgb
        )
        FontRenderers.Arial.drawString(context.matrices, description, x.toFloat(), y.toFloat(), Colors.WHITE)
    }

    override fun drawRectWhite(context: DrawContext, mouseX: Int, mouseY: Int) {
        if(mc.currentScreen !is HUDGui) return
        context.fill(x,y,x+width,y+height, Color(255,255,255,if(hover(x,y,width,height,mouseX,mouseY)) 200 else 120).rgb )
    }


}
