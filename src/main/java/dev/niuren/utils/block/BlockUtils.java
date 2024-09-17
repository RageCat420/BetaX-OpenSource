package dev.niuren.utils.block;

import dev.niuren.mixins.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dev.niuren.systems.modules.Module.mc;
import static dev.niuren.systems.modules.combat.LegitClick.getPlaceSide;

/**
 * @author NiuRen0827
 * Time:15:48
 */
public class BlockUtils {
    public static boolean inside(PlayerEntity en, Box bb) {
        return mc.world.getBlockCollisions(en, bb).iterator().hasNext();
    }

    public static ArrayList<BlockPos> getSphere(float range, BlockPos pos) {
        ArrayList<BlockPos> list = new ArrayList<>();
        for (int x = pos.getX() - (int) (range + 1); x < pos.getX() + (int) (range + 1); ++x) {
            for (int y = pos.getY() - (int) (range + 1); y < pos.getY() + (int) (range + 1); ++y) {
                for (int z = pos.getZ() - (int) (range + 1); z < pos.getZ() + (int) (range + 1); ++z) {
                    BlockPos curPos = new BlockPos(x, y, z);
                    if (MathHelper.sqrt((float) pos.getSquaredDistance(curPos)) <= range) {
                        list.add(curPos);
                    }
                }
            }
        }
        return list;
    }

    public static float[] getRotate(BlockPos blockPos, Direction side) {
        if (side != null) {
            Vec3d directionVec = new Vec3d(blockPos.getX()
                + 0.5
                + side.getVector().getX()
                * 0.5, blockPos.getY() + 0.5
                + side.getVector().getY() * 0.5,
                blockPos.getZ()
                    + 0.5 + side.getVector().getZ()
                    * 0.5);
            return getLegitRotations(directionVec);
        }
        return null;
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.getYaw() + MathHelper.wrapDegrees(yaw - mc.player.getYaw()), mc.player.getPitch() + MathHelper.wrapDegrees(pitch - mc.player.getPitch())};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.getX(), mc.player.getY() + (double) mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());
    }

    public static float @NotNull [] calculateAngle(Vec3d to) {
        return calculateAngle(getEyesPos(), to);
    }

    public static float @NotNull [] calculateAngle(@NotNull Vec3d from, @NotNull Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((float) (difX * difX + difZ * difZ));

        float yD = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0);
        float pD = (float) MathHelper.clamp(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))), -90f, 90f);

        return new float[]{yD, pD};
    }

    public static boolean isInsideBlock() {
        BlockPos pos1 = new BlockPos(MathHelper.floor(mc.player.getX() + 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() + 0.2));
        BlockPos pos2 = new BlockPos(MathHelper.floor(mc.player.getX() - 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() + 0.2));
        BlockPos pos3 = new BlockPos(MathHelper.floor(mc.player.getX() + 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() - 0.2));
        BlockPos pos4 = new BlockPos(MathHelper.floor(mc.player.getX() - 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() - 0.2));
        return !mc.world.getBlockState(pos1).isAir() || !mc.world.getBlockState(pos2).isAir() || !mc.world.getBlockState(pos3).isAir() || !mc.world.getBlockState(pos4).isAir();
    }

    public static void clickBlock(BlockPos pos, Direction side) {
        Vec3d directionVec = new Vec3d(pos.getX() + 0.5 + side.getVector().getX() * 0.5, pos.getY() + 0.5 + side.getVector().getY() * 0.5, pos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, 0));
    }

    public static void placeBlock(BlockPos pos) {
        Direction side;
        if ((side = getPlaceSide(pos)) == null) return;
        mc.player.swingHand(Hand.MAIN_HAND);
        clickBlock(pos.offset(side), side.getOpposite());
    }

    public static int getWorldActionId(ClientWorld world) {
        PendingUpdateManager pum = getUpdateManager(world);
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld) world).acquirePendingUpdateManager();
    }
}
