package dev.niuren.systems.modules.player;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

/**
 * @author NiuRen0827
 * Time:15:02
 */
@Module.Info(name = "SilentEat", description = "SilentEat", category = Module.Category.Player, chineseName = "线程进食")
public class SilentEat extends Module {
    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerActionC2SPacket packet && packet.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM && mc.player.getActiveItem().getItem().isFood()) {
            event.cancel();
        }
    }
}
