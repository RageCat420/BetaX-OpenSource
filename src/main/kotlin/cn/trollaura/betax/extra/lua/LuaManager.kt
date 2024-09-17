package cn.trollaura.betax.extra.lua

import com.sun.security.ntlm.Client
import dev.niuren.BetaX
import dev.niuren.ic.Setting
import dev.niuren.ic.Settings
import dev.niuren.systems.modules.Module
import dev.niuren.systems.modules.Module.Info
import dev.niuren.systems.modules.Modules
import java.io.File
import java.util.*

object LuaManager {
    val MAIN_FOLDER = File(BetaX.MOD_ID)
    val file = File(MAIN_FOLDER.absolutePath + "\\luas\\")
    var luas = mutableListOf<Lua>()
    var settingses = mutableMapOf<String, MutableList<Setting<out Any>>>()

    fun init() {
        //Load luas
        loadLuas()
    }

    fun loadLuas() {
        luas.clear()
        if(Modules.get().modules != null) {
            Modules.get().removeAllLuas()
        }

        file.mkdirs()
        file.listFiles()?.forEach {
            println(it.name)
            val lua = Lua(it)
            val module  = Module(lua.name,lua.name,Module.Category.Lua,"No")

            lua.module = module
            luas.add(lua)
        }
        for (lua in luas) {
            for ((key, value) in settingses) {
                if (lua.name == key) {
                    value.forEach {
                        Settings.get().addSetting(it)
                    }
                }
            }
            lua.load()
            Modules.get().modules.add(lua.module)
            Modules.get().modules.forEach {
                it.getName2()
            }
        }
    }
}
