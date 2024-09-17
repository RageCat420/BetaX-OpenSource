package cn.trollaura.betax.systems.modules.hud

import cn.trollaura.betax.systems.modules.DragModule
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.util.Formatting


object TPS: DragModule("TPS","服务器刻",Category.HUD,"?",
    450,400,50,15) {



    override fun renderDragger(context: DrawContext) {
            val playerListEntry = mc.networkHandler!!.getPlayerListEntry(
                mc.player!!.uuid
            )
            val content = Formatting.GRAY.toString() + "Ping " + Formatting.WHITE + (playerListEntry?.latency
                ?: 0)
            context.drawTextWithShadow(mc.textRenderer, content, x, y, -1)
    }
}




