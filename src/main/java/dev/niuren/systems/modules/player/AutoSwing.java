package dev.niuren.systems.modules.player;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Hand;

/**
 * @author NiuRen0827
 * Time:14:04
 */
@Module.Info(name = "AutoSwing", category = Module.Category.Player, description = "Automatically swings your hand", chineseName = "自动挥手")
public class AutoSwing extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
