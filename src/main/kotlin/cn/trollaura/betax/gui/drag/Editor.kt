package cn.trollaura.betax.gui.drag

import dev.niuren.BetaX
import dev.niuren.gui.font.FontRenderers
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Colors.WHITE
import java.awt.Color

class Editor(val description: String): Dragger {
    override var dragging: Boolean = false
    override var dragX: Int = 0
    override var dragY: Int = 0

    var x = 300
    var y = 300
    var height = 10
    var width = 60

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int) {
        if(dragging) {
            x = dragX + mouseX
            y = dragY + mouseY
        }
        drawDescription(context!!,mouseX,mouseY,description)

        drawClick(context)
        drawRectWhite(context,mouseX,mouseY)
    }

    override fun renderDragger(context: DrawContext) {
        TODO("Not yet implemented")
    }

    override fun mouseDown(mouseX: Int, mouseY: Int, button: Int) {
        if(hover(x,y,width,height,mouseX,mouseY)) {

            if(button == 0) {
                MinecraftClient.getInstance().setScreen(BetaX.HUDGUI)
            }
            if(button == 1) {
                dragging = true
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

    fun drawClick(context:DrawContext) {
        val h = y
        val w = x
        context.fill(w,h,w+60,h+10,-1)
        FontRenderers.Arial.drawString(context.matrices, "Editor", w + 2.0, h + 2.0, WHITE)
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
