package dev.niuren.systems.modules.player;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:15:32
 */
@Module.Info(name = "AutoRespawn", category = Module.Category.Player, description = "Automatically respawns you when you die", chineseName = "自动重生")
public class AutoRespawn extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        if (mc.player.isDead()) {
            mc.player.setShowsDeathScreen(false);
            mc.player.requestRespawn();
        }
    }
}
