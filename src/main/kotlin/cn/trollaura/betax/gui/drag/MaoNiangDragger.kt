package cn.trollaura.betax.gui.drag

import cn.trollaura.betax.systems.modules.client.Component
import cn.trollaura.betax.systems.modules.client.Component.animationSpeed
import dev.niuren.BetaX
import dev.niuren.gui.font.FontRenderers
import dev.niuren.systems.modules.client.ClickGUI
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Colors.WHITE
import java.awt.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class MaoNiangDragger(var x:Int,  var y: Int,  var width: Int, var height: Int,val description: String): Dragger {
    override var dragging: Boolean = false
    override var dragX: Int = 0
    override var dragY: Int = 0

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int) {
        if(!Component.niuru.get()) return
        if (dragging) {
            x = animate(x.toDouble(),dragX + mouseX.toDouble()).toInt()
            y = animate(y.toDouble(),dragY + mouseY.toDouble()).toInt()
        }
        drawDescription(context!!,mouseX,mouseY,description)

        val gui = ClickGUI()
        gui.drawCatGirl(context, x,y)
        drawRectWhite(context,mouseX,mouseY)
    }

    fun animate(current: Double, endPoint: Double): Double {
        return animate(current, endPoint, animationSpeed.get().toDouble() / 10)
    }

    fun animate(current: Double, endPoint: Double, speed: Double): Double {
        val shouldContinueAnimation = endPoint > current
        val dif = max(endPoint, current) - min(endPoint, current)
        val factor = dif * speed
        return if (abs(factor) <= 0.001) endPoint else current + if (shouldContinueAnimation) factor else -factor
    }

    override fun renderDragger(context: DrawContext) {
        TODO("Not yet implemented")
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

    override fun drawDescription(context:DrawContext,mouseX: Int, mouseY: Int, description: String) {
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
        FontRenderers.Arial.drawString(context.matrices, description, x.toFloat(), y.toFloat(), WHITE)
    }

    override fun mouseUp(mouseX: Int, mouseY: Int) {
        dragging = false
    }

    override fun keyPress(key: Int) {
        TODO("Not yet implemented")
    }
    override fun drawRectWhite(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (!hover(x,y,width,height,mouseX,mouseY)) return
        context.fill(x,y,x+width,y+height,Color(255,255,255,120).rgb )
    }

    override fun close() {
        TODO("Not yet implemented")
    }




}
