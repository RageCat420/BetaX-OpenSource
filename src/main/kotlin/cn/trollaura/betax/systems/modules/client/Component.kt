package cn.trollaura.betax.systems.modules.client

import dev.niuren.events.event.RenderEvent
import dev.niuren.ic.Setting
import dev.niuren.systems.modules.Module
import dev.niuren.systems.modules.Module.Info
import dev.niuren.systems.modules.client.ClickGUI
import meteordevelopment.orbit.EventHandler

@Info(category = Module.Category.Client, name = "Component", chineseName = "组件", description = "draw?")
object Component: Module() {
    val niuru   = Setting("Niuren",this,true)
    val animationSpeed = Setting("AnimationSpeed",this,2,0,10,1,true)
    val rainbow = Setting("Rainbow",this,true)

}
