package dev.niuren.systems.modules.client

import dev.niuren.ic.Description
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module

@Module.Info(category = Module.Category.Client, name = "Test", chineseName = "测试", description = "不知道")
object Test: Module() {
    val description = Setting<Description>("Fuck U SB",this)
}
