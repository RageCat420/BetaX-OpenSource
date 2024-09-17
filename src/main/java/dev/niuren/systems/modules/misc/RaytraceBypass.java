package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.mixins.IPlayerMoveC2SPacket;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.player.MovementUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @author NiuRen0827
 * Time:20:27
 */
@Module.Info(name = "RaytraceBypass", chineseName = "光线追踪绕过", category = Module.Category.Misc, description = "raytrace bypass")
public class RaytraceBypass extends Module {
    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (MovementUtil.lastPitch != -91f) {
                ((IPlayerMoveC2SPacket) packet).setPitch(-91);
            }
        }
    }
}
