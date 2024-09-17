package cn.trollaura.betax.gui.button.settings

import dev.niuren.BetaX
import dev.niuren.gui.button.SettingButton
import dev.niuren.gui.font.FontRenderers
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module
import net.minecraft.SharedConstants
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import java.awt.Color


/**
@author tRollaURa_
@since 2024/9/16
 */
@SuppressWarnings("FieldCanBeLocal")
class StringSetting(val module: Module, val setting: Setting<String>, val x: Int, val y:Int, val w: Int, val h:Int): SettingButton(module, x, y, w, h)  {
    val string = CurrentString("")
    var listenering = false
    var width_ = 0.0

    fun removeLastChar(str: String): String {
        var output = ""
        if (str.isNotEmpty()) {
            output = str.substring(0, str.length - 1)
        }
        return output
    }
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {

        drawButton(context)
        val stack = MatrixStack()
        stack.scale(1.0f, 1.0f, 1.0f)
        width_ = animate(width_, (if (listenering) x() + w() - 3 else x() + 3).toDouble())
        BetaX.GUI.drawFlat(
            context,
            x() + 3,
            y() + 1,
            width_.toInt(),
            y() + h(),
            if(hover(x(),y(),w(),h(),mouseX,mouseY)) Color(WHITE).darker().rgb else Color(WHITE).rgb
        )
        if(listenering) {
            FontRenderers.Arial.drawString(
                context.matrices,
                string.string,
                (x() + 6).toFloat(),
                (y() + 4).toFloat(),
                WHITE
            )
        }else {
            FontRenderers.Arial.drawString(
                context.matrices,
                setting.get(),
                (x() + 6).toFloat(),
                (y() + 4).toFloat(),
                WHITE
            )
        }

    }

    override fun keyPress(key: Int) {
        if(!listenering) return
        when(key) {
            1 -> {
                enter()
                return
            }
            0 -> {
                enter()
                return
            }
            GLFW.GLFW_KEY_ENTER -> {
                enter()
            }
            GLFW.GLFW_KEY_BACKSPACE -> {
                string.string = removeLastChar(string.string)
            }
        }


    }

    override fun keyTyped(charW: Char, key: Int) {
        if(listenering) {
            if(SharedConstants.isValidChar(charW)) {
                string.string += charW
            }

        }
    }


    override fun mouseDown(mouseX: Int, mouseY: Int, button: Int) {
        if(hover(x(),y(),w(),h(),mouseX,mouseY)) {
            listenering = !listenering
        }
    }

    fun enter() {
        if(listenering.not()) return
        setting.setValue(string.string)
        listenering = false
        this.string.string = ""
    }

}
