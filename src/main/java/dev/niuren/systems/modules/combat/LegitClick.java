package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.MotionEvent;
import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.StrafeFix;
import dev.niuren.utils.block.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Iterator;

import static dev.niuren.utils.block.BlockUtil.canPlace;
import static dev.niuren.utils.block.BlockUtils.getRotate;

/**
 * @author NiuRen0827
 * Time:19:52
 */
@Module.Info(name = "LegitClick", category = Module.Category.Combat, description = "Legit click", chineseName = "合法引爆")
public class LegitClick extends Module {
    private final Setting<Integer> range = register("Range", 5, 1, 10);
    BlockPos blockPos = null;

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) {
            return;
        }
        for (BlockPos pos : BlockUtils.getSphere(5, mc.player.getBlockPos())) {
            if (mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                breakCrystal(pos.up());
                blockPos = pos.up();
            }
        }
    }

    @EventHandler
    public void onMotion(MotionEvent event) {
        if (nullCheck()) {
            return;
        }
        StrafeFix strafeFix = Modules.get().get(StrafeFix.class);
        if (blockPos != null && !strafeFix.toggled && canPlace(blockPos, range.get())) {
            if (getRotate(blockPos, getPlaceSide(blockPos)) == null) return;
            float[] angle = getRotate(blockPos, getPlaceSide(blockPos));
            event.setYaw(angle[0]);
            event.setPitch(angle[1]);
        } else {
            if (blockPos != null) {
                if (getRotate(blockPos, getPlaceSide(blockPos)) == null && canPlace(blockPos, range.get())) return;
                float[] angle = getRotate(blockPos, getPlaceSide(blockPos));
                if (angle == null) {
                    return;
                }
                strafeFix.setRotation(angle[0], angle[1]);
            }
        }
    }

    public void breakCrystal(BlockPos pos) {
        Hand hand = Hand.MAIN_HAND;
        Iterator<EndCrystalEntity> endCrystalEntityIterator = mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1))).iterator();
        if (endCrystalEntityIterator.hasNext()) {
            EndCrystalEntity crystal = endCrystalEntityIterator.next();
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
            mc.player.attack(crystal);
            mc.player.swingHand(hand);
        }
    }

    public static boolean canClick(BlockPos pos) {
        return mc.world.getBlockState(pos).isSolid();
    }

    public static Direction getPlaceSide(BlockPos pos) {
        Direction side = null;
        for (Direction i : Direction.values()) {
            if (pos != null) {
                if (canClick(pos.offset(i))) {
                    side = i;
                    break;
                }
            }
        }
        return side;
    }
}
