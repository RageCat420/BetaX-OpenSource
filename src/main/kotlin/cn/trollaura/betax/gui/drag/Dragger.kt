package cn.trollaura.betax.gui.drag

import cn.trollaura.betax.gui.HUDGui.height
import cn.trollaura.betax.gui.HUDGui.width
import dev.niuren.gui.Component
import dev.niuren.utils.render.Render2DUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Colors.WHITE
import java.awt.Color



/**
@author tRollaURa_
 **/
interface Dragger: Component {
    var dragging: Boolean
    var dragX: Int
    var dragY: Int
    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int) {}
    fun renderDragger(context: DrawContext)

    override fun mouseDown(mouseX: Int, mouseY: Int, button: Int)
    fun drawDescription(context: DrawContext,mouseX: Int,mouseY: Int,description: String)

    fun drawRectWhite(context: DrawContext,mouseX: Int,mouseY: Int)
    fun hover(X: Int, Y: Int, W: Int, H: Int, mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= X * 1.0f && mouseX <= (X + W) * 1.0f && mouseY >= Y * 1.0f && mouseY <= (Y + H) * 1.0f
    }
    override fun mouseUp(mouseX: Int, mouseY: Int)

    override fun keyPress(key: Int) {}
    override fun close() {}
}
