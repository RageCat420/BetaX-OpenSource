package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.player.InventoryUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author NiuRen0827
 * Time:13:46
 */
@Module.Info(name = "WallClip", category = Module.Category.Misc, description = "Allows you to clip in walls", chineseName = "珍珠卡墙")
public class WallClip extends Module {
    @EventHandler
    public void oonTick(TickEvent.Post event) {
        if (nullCheck()) return;
        int slot = InventoryUtils.findItem(Items.ENDER_PEARL);
        if (slot == -1) return;
        InventoryUtils.doSwap(slot);
        if (mc.options.sneakKey.isPressed())
            return;
        int prevItem = mc.player.getInventory().selectedSlot;
        Vec3d targetPos = new Vec3d(mc.player.getX() + MathHelper.clamp(roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.241, Math.floor(mc.player.getX()) + 0.759) - mc.player.getX(), -0.03, 0.03), mc.player.getY(), mc.player.getZ() + MathHelper.clamp(roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.241, Math.floor(mc.player.getZ()) + 0.759) - mc.player.getZ(), -0.03, 0.03));
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(EntitiesUtil.getLegitRotations(targetPos)[0],80, true));
        InventoryUtils.doSwap(slot);
        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
        InventoryUtils.doSwap(prevItem);
        toggle();
    }
    private double roundToClosest(double num, double low, double high) {
        double d1 = num - low;
        double d2 = high - num;

        if (d2 > d1) {
            return low;

        } else {
            return high;
        }
    }
}
