package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

/**
 * @author NiuRen0827
 * Time:14:26
 */
@Module.Info(name = "ClickTp", category = Module.Category.Movement, description = "Allows you to teleport by clicking", chineseName = "点击传送")
public class ClickTp extends Module {
    private final Setting<Double> maxDistance = register("Max Distance", 5.0, 0.0, 20.0);
    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.isUsingItem()) return;

        if (mc.options.useKey.isPressed()) {
            HitResult hitResult = mc.player.raycast(maxDistance.get(), 1f / 20f, false);

            if (hitResult.getType() == HitResult.Type.ENTITY && mc.player.interact(((EntityHitResult) hitResult).getEntity(), Hand.MAIN_HAND) != ActionResult.PASS)
                return;

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                Direction side = ((BlockHitResult) hitResult).getSide();

                if (mc.world.getBlockState(pos).onUse(mc.world, mc.player, Hand.MAIN_HAND, (BlockHitResult) hitResult) != ActionResult.PASS)
                    return;

                BlockState state = mc.world.getBlockState(pos);

                VoxelShape shape = state.getCollisionShape(mc.world, pos);
                if (shape.isEmpty()) shape = state.getOutlineShape(mc.world, pos);

                double height = shape.isEmpty() ? 1 : shape.getMax(Direction.Axis.Y);

                mc.player.setPosition(pos.getX() + 0.5 + side.getOffsetX(), pos.getY() + height, pos.getZ() + 0.5 + side.getOffsetZ());
            }
        }
    }
}
