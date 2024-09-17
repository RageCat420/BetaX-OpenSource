package cn.trollaura.betax.gui


import cn.trollaura.betax.systems.modules.DragModule
import cn.trollaura.betax.systems.modules.hud.*
import dev.niuren.gui.Gui
import dev.niuren.gui.Window
import dev.niuren.systems.modules.Module
import dev.niuren.systems.modules.Modules
import dev.niuren.systems.modules.client.ClickGUI
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object HUDGui: Screen(Text.literal("HUDGui")) {
    var windows = mutableListOf<Window>()
    var draggers = mutableListOf<DragModule>()

    init {
        windows.add(Window(Module.Category.HUD, 5, 3, 95, 15))
        draggers.add(Armor)
        draggers.add(Coords)
        draggers.add(Durability)
        draggers.add(FPS)
        draggers.add(Speed)
        draggers.add(Watermark)

    }
    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        windows.forEach {
            it.render(context,mouseX,mouseY)
        }
        draggers.forEach {
            it.mouseX = mouseX
            it.mouseY = mouseY
            it.renderDragger(context!!)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        windows.forEach { window: Window ->
            window.mouseDown(
                mouseX.toInt(),
                mouseY.toInt(),
                button
            )
        }
        draggers.forEach {

            it.mouseDown(mouseX.toInt(),mouseY.toInt(),button)
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        windows.forEach {
            it.mouseUp(mouseX.toInt(),mouseY.toInt())
        }
        draggers.forEach {
            it.mouseUp(mouseX.toInt(),mouseY.toInt())
        }
        return false
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        val gui = Modules.get().get(ClickGUI::class.java)

        if (verticalAmount < 0) {
            for (window in Gui.windows) {
                window.y(window.y() - gui.scrollFactor.get())
            }
        } else if (verticalAmount > 0) {
            for (window in Gui.windows) {
                window.y(window.y() + gui.scrollFactor.get())
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
}
