package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

/**
 * @author NiuRen0827
 * Time:20:31
 */
@Module.Info(name = "NoInteract", chineseName = "无箱子交互", category = Module.Category.Misc, description = "NoInteract")
public class NoInteract extends Module {
    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (nullCheck() || !(event.getPacket() instanceof PlayerInteractBlockC2SPacket packet)) {
            return;
        }
        Block block = mc.world.getBlockState(packet.getBlockHitResult().getBlockPos()).getBlock();
        if (!mc.player.isSneaking()) {
            if (block instanceof ChestBlock || block instanceof EnderChestBlock) {
                event.cancel();
            }
        }
    }
}
