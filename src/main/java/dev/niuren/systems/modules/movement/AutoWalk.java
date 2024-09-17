package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:13:47
 */
@Module.Info(name = "AutoWalk", description = "AutoWalk", category = Module.Category.Movement, chineseName = "自动行走")
public class AutoWalk extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event){
        mc.options.forwardKey.setPressed(true);
    }
}
