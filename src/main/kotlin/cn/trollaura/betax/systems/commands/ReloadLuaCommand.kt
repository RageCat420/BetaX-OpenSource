package cn.trollaura.betax.systems.commands

import cn.trollaura.betax.extra.lua.LuaManager
import dev.niuren.BetaX
import dev.niuren.gui.Gui
import dev.niuren.ic.Command

object ReloadLuaCommand: Command(arrayOf("reloadLua")) {
    override fun onCommand(command: String?, args: Array<out String>?) {
        LuaManager.init()
    }
}
