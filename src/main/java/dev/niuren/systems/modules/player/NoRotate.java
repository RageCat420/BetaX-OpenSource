package dev.niuren.systems.modules.player;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.mixins.IPlayerPositionLookS2CPacket;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

/**
 * @author NiuRen0827
 * Time:14:35
 */
@Module.Info(name = "NoRotate", category = Module.Category.Player, description = "Allows you to modify your rotation", chineseName = "转头重置")
public class NoRotate extends Module {
    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            ((IPlayerPositionLookS2CPacket) packet).setYaw(mc.player.getYaw());
            ((IPlayerPositionLookS2CPacket) packet).setPitch(mc.player.getPitch());
        }
    }
}
