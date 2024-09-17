package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import dev.niuren.gui.font.FontRenderer
import dev.niuren.gui.font.FontRenderers
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module
import net.minecraft.client.gui.DrawContext
import java.awt.Color

object Watermark: DragModule("WaterMark","水印",Category.HUD,"?",100,100,100,100) {

    val r = Setting("Red",this,255,0,255,1,true)
    val g = Setting("Green",this,255,0,255,1,true)
    val b = Setting("Blue",this,255,0,255,1,true)
    val font = Setting("Font",this, listOf("FoughtKnight","New"),"FoughtKnight")

    override fun renderDragger(context: DrawContext) {
        test()
        drag()
        when(font.get()) {
            "New" -> {
                FontRenderers.NewLogo.drawString(context.matrices,"BetaX",x.toFloat(),y.toFloat(),Color(r.get(), g.get(), b.get()).rgb,false)
            }
            "FoughtKnight" -> {
                FontRenderers.FoughtKnight.drawString(context.matrices,"BetaX",x.toFloat(),y.toFloat(),Color(r.get(), g.get(), b.get()).rgb,false)
            }
        }
        drawDescription(context,mouseX,mouseY,description)
        drawRectWhite(context,mouseX,mouseY)
    }



}
