package cn.trollaura.betax.systems.modules.render

import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module

object SwordBlock: Module("SwordBlock","1.8格挡",Category.Render,"") {
    @JvmStatic
    val x = Setting("X",this,0.14142136,0.0,1.0,1)
    @JvmStatic
    val y = Setting("Y",this,0.08,0.0,1.0,1)
    @JvmStatic
    val z = Setting("Z",this,0.14142136,0.0,1.0,1)
}
