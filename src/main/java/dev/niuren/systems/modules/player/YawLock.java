package dev.niuren.systems.modules.player;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:11:07
 */
@Module.Info(name = "YawLock", category = Module.Category.Player, description = "", chineseName = "横向转头锁定")
public class YawLock extends Module {
    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.player != null) {
            mc.player.setYaw(45);
        }
    }
}
