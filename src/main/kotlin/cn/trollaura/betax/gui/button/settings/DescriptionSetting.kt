package cn.trollaura.betax.gui.button.settings

import dev.niuren.BetaX
import dev.niuren.gui.button.SettingButton
import dev.niuren.gui.font.FontRenderers
import dev.niuren.ic.Description
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color


class DescriptionSetting(val module: Module,val setting: Setting<Description>,val x: Int,val y:Int,val w: Int,val h:Int): SettingButton(module, x, y, w, h) {



    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {

        drawButton(context)
        val stack = MatrixStack()
        stack.scale(1.0f, 1.0f, 1.0f)
            BetaX.GUI.drawFlat(
                context,
                x() + 3,
                y() + 1,
                x() + w() - 3,
                y() + h(),
                Color(BLACK).rgb
            )
        FontRenderers.Arial.drawString(
            context.matrices,
            setting.name,
            (x() + 6).toFloat(),
            (y() + 4).toFloat(),
            WHITE
        )
    }

}
