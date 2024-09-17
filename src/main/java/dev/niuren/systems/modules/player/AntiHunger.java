package dev.niuren.systems.modules.player;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.mixins.IPlayerMoveC2SPacket;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.NotNull;

/**
 * @author NiuRen0827
 * Time:19:43
 */
@Module.Info(name = "AntiHunger", description = "", category = Module.Category.Player, chineseName = "抗饥饿")
public class AntiHunger extends Module {
    @EventHandler
    public void onPacketSend(PacketEvent.@NotNull Send e) {
        if (e.packet instanceof PlayerMoveC2SPacket pac) {
            ((IPlayerMoveC2SPacket) pac).setOnGround(false);
        }

        if (e.packet instanceof ClientCommandC2SPacket pac) {
            if (pac.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
                e.cancel();
                mc.player.setSprinting(false);
            }
        }
    }
}
