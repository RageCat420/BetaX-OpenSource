package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.ic.Setting;
import dev.niuren.mixins.IEntityVelocityUpdateS2CPacket;
import dev.niuren.mixins.IExplosionS2CPacket;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.math.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import static dev.niuren.utils.block.BlockUtils.isInsideBlock;

/**
 * @author NiuRen0827
 * Time:14:25
 */
@Module.Info(name = "Velocity", category = Module.Category.Movement, description = "Allows you to modify your velocity", chineseName = "反击退")
public class Velocity extends Module {
    private final Setting<Boolean> grim = register("Grim", false);
    public final Setting<Boolean> blockPush = register("BlockPush", true);
    private final Setting<Integer> horizontal = register("Horizontal", 0, 0, 100);
    private final Setting<Integer> vertical = register("Vertical", 0, 0, 100);
    private final Timer lagBackTimer = new Timer();

    @EventHandler
    public void onPacketEvent(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            lagBackTimer.reset();
        }
    }
    @EventHandler
    public void onReceivePacket(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (grim.get()){
            if (!lagBackTimer.passed(100)) {
                return;
            }
            if (event.getPacket() instanceof ExplosionS2CPacket explosion) {
                ((IExplosionS2CPacket) explosion).setX(0);
                ((IExplosionS2CPacket) explosion).setY(0);
                ((IExplosionS2CPacket) explosion).setZ(0);
            }
        }
        if (grim.get() && !isInsideBlock()) return;
        float h = (float) horizontal.get() / 100;
        float v = (float) vertical.get() / 100;

        if (event.packet instanceof EntityStatusS2CPacket packet && (packet = (EntityStatusS2CPacket) event.packet).getStatus() == 31 && packet.getEntity(mc.world) instanceof FishingBobberEntity fishHook) {
            if (fishHook.getHookedEntity() == mc.player) {
                event.cancel();
            }
        }

        if (event.packet instanceof ExplosionS2CPacket) {
            IExplosionS2CPacket packet = (IExplosionS2CPacket) event.packet;

            packet.setX(packet.getX() * h);
            packet.setY(packet.getY() * v);
            packet.setZ(packet.getZ() * h);
            return;
        }

        if (event.packet instanceof EntityVelocityUpdateS2CPacket packet) {
            if (packet.getId() == mc.player.getId()) {
                if (horizontal.get() == 0 && vertical.get() == 0) {
                    event.cancel();
                } else {
                    ((IEntityVelocityUpdateS2CPacket) packet).setX((int) (packet.getVelocityX() * h));
                    ((IEntityVelocityUpdateS2CPacket) packet).setY((int) (packet.getVelocityY() * v));
                    ((IEntityVelocityUpdateS2CPacket) packet).setZ((int) (packet.getVelocityZ() * h));
                }
            }
        }
    }
}
