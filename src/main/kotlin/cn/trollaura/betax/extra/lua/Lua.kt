package cn.trollaura.betax.extra.lua

import dev.niuren.BetaX
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import org.luaj.vm2.lib.jse.LuajavaLib
import java.io.File
import java.util.function.Predicate

/**
 * @author tRollaURa_!!!!!
 */

class Lua(val file: File) {
    lateinit var module: Module
    var name = "UNKNOWN"
    var settings = mutableListOf<Setting<out Any>>()
    var author = "UNKNOWN"
    var globals = JsePlatform.standardGlobals()
    lateinit var chunk: LuaValue

    fun load() {
        try {
            globals.set("register_boolean_setting",register_boolean_setting(settings,module))
            globals.set("register_slider_setting",register_slider_setting(settings,module))
            globals.load(LuajavaLib())

            val getNameFunc = globals["getName"]

            if (getNameFunc.isfunction()) {
                name = getNameFunc.call().toString()
            } else {
                name = "Unknown Name"
            }
            val init = globals["init_lua"]
            if (init.isfunction()) {
                init.call()
            }
        } catch (e: Exception) {
            BetaX.LOG.error("An error occurred while reading lua file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    fun init() {
        chunk.call("init_lua")
    }


    class register_boolean_setting(val settings: MutableList<Setting<out Any>>,val module: Module): VarArgFunction() {
        override fun invoke(args: Varargs): LuaValue {
            val name = args.checkjstring(1)
            val value = args.checkboolean(2)
            val setting = Setting(name,module,value)
            settings.add(setting)
            return CoerceJavaToLua.coerce(setting)
        }
    }
    class register_slider_setting(val settings: MutableList<Setting<out Any>>,val module: Module): VarArgFunction() {
        override fun invoke(args: Varargs): LuaValue {
            val name = args.checkjstring(1)
            val value = args.checknumber(2)
            val min = args.checknumber(3)
            val max = args.checknumber(4)
            val inc = args.checkint(5)
            val setting = Setting(name,module,value,min,max,inc)
            settings.add(setting)
            return CoerceJavaToLua.coerce(setting)
        }
    }


}
