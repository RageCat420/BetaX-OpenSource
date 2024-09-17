package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.MotionEvent;
import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Friends;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.StrafeFix;
import dev.niuren.utils.block.BlockUtil;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.player.InventoryUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import static dev.niuren.utils.block.BlockUtil.getBlock;
import static dev.niuren.utils.block.BlockUtils.placeBlock;

/**
 * @author NiuRen0827
 * Time:19:09
 */
@Module.Info(name = "WebAura", category = Module.Category.Combat, description = "Places webs around you.", chineseName = "蜘蛛网光环")
public class WebAura extends Module {
    private final Setting<Boolean> rotate = register("Rotate", false);
    private final Setting<Boolean> facePlace = register("FacePlace", false);
    private final Setting<Integer> range = register("Range", 5, 1, 10);

    private BlockPos blockPos = null;

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) {
            return;
        }
        int web = InventoryUtils.findItem(Items.COBWEB);
        int old = mc.player.getInventory().selectedSlot;
        if (web == -1) {
            return;
        }
        for (PlayerEntity target : EntitiesUtil.getEnemies(range.get())) {
            if (!target.isAlive()) return;
            if (target.equals(mc.player)) return;
            if (Friends.get().isFriend(target)){
                return;
            }
            BlockPos blockPos = target.getBlockPos();
            BlockPos blockPos1 = target.getBlockPos().up();
            if (getBlock(blockPos) == Blocks.AIR && blockPos != null) {
                InventoryUtils.doSwap(web);
                placeBlock(blockPos);
                if (facePlace.get()) {
                    InventoryUtils.doSwap(web);
                    placeBlock(blockPos1);
                }
            }
        }
        InventoryUtils.doSwap(old);
    }

    @EventHandler
    public void onMotionUpdate(MotionEvent event) {
        if (nullCheck()) {
            return;
        }
        if (blockPos == null) return;
        float[] angle = BlockUtil.getLegitRotations(blockPos.toCenterPos());
        StrafeFix strafeFix = Modules.get().get(StrafeFix.class);
        if (rotate.get() && strafeFix.toggled) {
            strafeFix.setRotation(angle[0], angle[1]);
        } else if (rotate.get()) {
            event.setYaw(angle[0]);
            event.setPitch(angle[1]);
        }
    }

    @Override
    public void onDeactivate() {
        blockPos = null;
    }
}
