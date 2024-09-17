package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.mixins.IPlayerMoveC2SPacket;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @author NiuRen0827
 * Time:15:28
 */
@Module.Info(name = "NoFall", category = Module.Category.Movement, description = "Allows you to modify your fall damage", chineseName = "无摔落伤害")
public class NoFall extends Module {
    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (nullCheck()) {
            return;
        }
        for (ItemStack is : mc.player.getArmorItems()) {
            if (is.getItem() == Items.ELYTRA) {
                return;
            }
        }
        if (event.packet instanceof PlayerMoveC2SPacket packet) {
            if (mc.player.fallDistance >= 0.1) {
                ((IPlayerMoveC2SPacket) packet).setOnGround(true);
            }
        }
    }
}
