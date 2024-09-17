package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import dev.niuren.mixins.IMinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.util.Formatting


object FPS: DragModule("FPS","帧数显示",Category.HUD,"?",
    500,300,50,15) {



    override fun renderDragger(context: DrawContext) {
        test()
        drag()
            val content =  "${Formatting.GRAY}FPS ${Formatting.WHITE}${IMinecraftClient.getFps()}"
            context.drawTextWithShadow(mc.textRenderer, content, x, y, -1)
        drawDescription(context,mouseX,mouseY,description)
        drawRectWhite(context,mouseX,mouseY)
    }



}
