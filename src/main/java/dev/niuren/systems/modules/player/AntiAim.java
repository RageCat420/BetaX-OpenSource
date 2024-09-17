package dev.niuren.systems.modules.player;

import dev.niuren.events.event.MotionEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:14:37
 */
@Module.Info(name = "AntiAim", category = Module.Category.Player, description = "", chineseName = "防止锁定")
public class AntiAim extends Module {
    @EventHandler
    public void onMotion(MotionEvent event) {
        event.setPitch(90);
        for (int i = 0; i <= 180; i++) {
            event.setYaw(i);
        }
    }
}
