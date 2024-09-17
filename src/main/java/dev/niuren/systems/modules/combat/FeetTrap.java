package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.MotionEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.StrafeFix;
import dev.niuren.utils.block.BlockUtils;
import dev.niuren.utils.player.InventoryUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static dev.niuren.utils.block.BlockUtil.canPlace;
import static dev.niuren.utils.block.BlockUtils.getLegitRotations;

/**
 * @author NiuRen0827
 * Time:20:18
 */
@Module.Info(name = "FeetTrap", description = "FeetTrap", category = Module.Category.Combat, chineseName = "围脚")
public class FeetTrap extends Module {
    private final Setting<Boolean> autoSwitch = register("AutoSwitch", false);
    private final Setting<Boolean> sync = register("Sync", true);
    private final Setting<Integer> syncRange = register("syncRange", 5,1,10);
    private final Setting<Boolean> predict = register("Predict", false);
    private final Setting<Boolean> rotate = register("Rotate", true);
    private Vec3d vec3d = null;
    private BlockPos pos = null;

    @EventHandler
    public void onMotionUpdate(MotionEvent event) {
        if (nullCheck()) {
            return;
        }
        int obsidian = InventoryUtils.findItem(Item.fromBlock(Blocks.OBSIDIAN));
        int oldSlot = mc.player.getInventory().selectedSlot;
        if (obsidian == -1) {
            return;
        }
        mc.player.setSprinting(false);
        for (Direction i : Direction.values()) {
            if (i.equals(Direction.DOWN) || i.equals(Direction.UP)) {
                continue;
            }
            BlockPos currentPos = mc.player.getBlockPos();
            BlockPos blockPos = currentPos.offset(i);
            BlockPos blockPos2 = null;
            StrafeFix strafeFix = Modules.get().get(StrafeFix.class);
            if (canPlace(blockPos, 2)) {
                InventoryUtils.doSwap(obsidian);
                blockPos2 = blockPos.offset(i);
                this.vec3d = blockPos2.toCenterPos();
                this.pos = blockPos;
                BlockUtils.placeBlock(blockPos);

                if (rotate.get() && strafeFix.toggled && vec3d != null && canPlace(BlockPos.ofFloored(vec3d), 2)) {
                    float[] angle = getLegitRotations(pos.toCenterPos());
                    strafeFix.setRotation(angle[0], angle[1]);
                } else if (rotate.get() && vec3d != null && canPlace(BlockPos.ofFloored(vec3d), 2)) {
                    float[] angle = getLegitRotations(pos.toCenterPos());
                    event.setYaw(angle[0]);
                    event.setPitch(angle[1]);
                }
            }
            if (predict.get()) {
                for (Direction i2 : Direction.values()) {
                    if (i2.equals(Direction.DOWN) || i2.equals(Direction.UP)) {
                        continue;
                    }
                    if (blockPos2 != null && canPlace(blockPos.offset(i), 2)) {
                        InventoryUtils.doSwap(obsidian);
                        if (sync.get()){
                            crystalSync();
                        }
                        BlockUtils.placeBlock(blockPos.offset(i2));
                        vec3d = blockPos2.toCenterPos();
                    }
                }
            }
        }
        if (autoSwitch.get()) {
            InventoryUtils.doSwap(oldSlot);
        }
    }
    private void crystalSync() {
        for (Entity target : mc.world.getEntities()) {
            if (target instanceof EndCrystalEntity) {
                if (mc.player.distanceTo(target) < syncRange.get()) {
                    mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                }
            }
        }
    }
}

