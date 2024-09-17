package dev.niuren.utils.block;

import dev.niuren.ic.Command;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.niuren.BetaX.mc;

/**
 * @author NiuRen0827
 * Time:21:26
 */
public class BlockUtil {
    public static final List<Block> shiftBlocks = Arrays.asList(
        Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
        Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
        Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
        Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
        Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
        Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static float getHardness(Block block) {return block.getHardness();}
    public static float getHardness(BlockPos block) {
        return mc.world.getBlockState(block).getHardness(mc.world, block);
    }
    public static boolean isAir(BlockPos block) {
        return mc.world.getBlockState(block).isAir();
    }

    public static boolean isBlastResist(BlockPos block) {return getBlastResistance(block) >= 600;}
    public static boolean isBlastResist(Block block) {return getBlastResistance(block) >= 600;}
    public static boolean isBreakable(BlockPos pos) {
        return getHardness(pos) > 0;
    }
    public static boolean isBreakable(Block block) {return getHardness(block) > 0;}
    public static boolean isCombatBlock(BlockPos block) {return isBlastResist(block) && isBreakable(block);}
    public static boolean isCombatBlock(Block block) {return isBlastResist(block) && isBreakable(block);}
    public static float getBlastResistance(BlockPos block) {
        return mc.world.getBlockState(block).getBlock().getBlastResistance();
    }
    public static boolean canPhase(BlockPos block){
        if(!isAir(block)){
            BlockPos pos = BlockPos.ofFloored(block.toCenterPos());
            if(mc.player.getPos().squaredDistanceTo(pos.toCenterPos())<=1.3){
                return true;
            }
        }
        return false;
    }
    public static float getBlastResistance(Block block) {
        return block.getBlastResistance();
    }
    public static boolean isOurSurround(BlockPos blockPos) {
        BlockPos pos = mc.player.getBlockPos();
        for (Direction dir : Direction.values()) {
            if (dir == Direction.UP || dir == Direction.DOWN) continue;
            if (pos.offset(dir).equals(blockPos)) return true;
        }
        return false;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(MathHelper.floor(mc.player.getX()), MathHelper.floor(mc.player.getY()), MathHelper.floor(mc.player.getZ()));
    }

    public static boolean canPlace(BlockPos pos, double distance) {
        if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) > distance)
            return false;
        if (!canBlockFacing(pos)) return false;
        if (!canReplace(pos)) return false;
        return !checkEntity(pos);
    }

    public static boolean checkEntity(BlockPos pos) {
        for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (entity instanceof EndCrystalEntity) return false;
            if (!entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity)
                continue;
            return true;
        }
        return false;
    }

    public static final ArrayList<BlockPos> placedPos = new ArrayList<>();

    public static void placeBlock(BlockPos pos, boolean rotate) {
        Direction side;
        if ((side = getPlaceSide(pos)) == null) return;
        placedPos.add(pos);
        boolean sneak = shiftBlocks.contains(getBlock(pos)) && !mc.player.isSneaking();
        mc.player.swingHand(Hand.MAIN_HAND);
        if (sneak)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        clickBlock(pos.offset(side), side.getOpposite(), rotate);
        if (sneak)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.getX(), mc.player.getY() + (double) mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());
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

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate) {
        Vec3d directionVec = new Vec3d(pos.getX() + 0.5 + side.getVector().getX() * 0.5, pos.getY() + 0.5 + side.getVector().getY() * 0.5, pos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
        if (rotate) {
            float[] angle = getLegitRotations(directionVec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, -1));
    }

    public static boolean canBlockFacing(BlockPos pos) {
        return getPlaceSide(pos) != null;
    }

    public static Direction getPlaceSide(BlockPos pos) {
        Direction side = null;
        for (Direction i : Direction.values()) {
            if (canClick(pos.offset(i))) {
                side = i;
                break;
            }
        }
        return side;
    }

    public static Direction getClickSide() {
        Direction side = null;
        for (Direction i : Direction.values()) {
            side = i;
            break;
        }
        return side;
    }


    public static ArrayList<BlockPos> getSphere(float range) {
        return getSphere(range, BlockUtil.getPlayerPos());
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

    public static BlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static boolean canReplace(BlockPos pos) {
        return getState(pos).isReplaceable();
    }

    public static boolean canClick(BlockPos pos) {
        return mc.world.getBlockState(pos).isSolid();
    }

    public static boolean breakBlock(BlockPos pos) {
        if (!canBreak(pos, mc.world.getBlockState(pos))) return false;
        BlockPos bp = pos instanceof BlockPos.Mutable ? new BlockPos(pos) : pos;

        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, bp, Direction.UP));
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, bp, Direction.UP));

        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        return true;
    }

    public static boolean canBreak(BlockPos blockPos, BlockState state) {
        if (!mc.player.isCreative() && state.getHardness(mc.world, blockPos) < 0) return false;
        return state.getOutlineShape(mc.world, blockPos) != VoxelShapes.empty();
    }

    public static Direction getClickSide(BlockPos pos) {
        if (pos.equals(BlockUtil.getPlayerPos())) {
            return Direction.UP;
        }
        Direction side = null;
        double range = 100;
        for (Direction i : Direction.values()) {
            if (canSee(pos, i)) continue;
            if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())) > range) continue;
            side = i;
            range = MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()));
        }
        if (side != null)
            return side;
        side = Direction.UP;
        for (Direction i : Direction.values()) {
            if (!isStrictDirection(pos, i)) continue;

            if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())) > range) continue;
            side = i;
            range = MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()));
        }
        return side;
    }

    public static Direction getClickSideStrict(BlockPos pos) {
        Direction side = null;
        double range = 100;
        for (Direction i : Direction.values()) {
            if (canSee(pos, i)) continue;
            if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())) > range) continue;
            side = i;
            range = MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()));
        }
        if (side != null)
            return side;
        side = null;
        for (Direction i : Direction.values()) {
            if (!isStrictDirection(pos, i)) continue;

            if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos())) > range) continue;
            side = i;
            range = MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.offset(i).toCenterPos()));
        }
        return side;
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side) {
        BlockState blockState = mc.world.getBlockState(pos);
        boolean isFullBox = blockState.getBlock() == Blocks.AIR || blockState.isFullCube(mc.world, pos) || getBlock(pos) == Blocks.COBWEB;
        return isStrictDirection(pos, side, isFullBox);
    }

    public static boolean isStrictDirection(BlockPos pos, Direction side, boolean isFullBox) {
        if (BlockUtil.getEyesPos().getY() - pos.getY() >= 0 && side == Direction.DOWN) return false;
        if (getBlock(pos.offset(side)) == Blocks.OBSIDIAN || getBlock(pos.offset(side)) == Blocks.BEDROCK || getBlock(pos.offset(side)) == Blocks.RESPAWN_ANCHOR)
            return false;
        Vec3d eyePos = BlockUtil.getEyesPos();
        Vec3d blockCenter = pos.toCenterPos();
        ArrayList<Direction> validAxis = new ArrayList<>();
        validAxis.addAll(checkAxis(eyePos.x - blockCenter.x, Direction.WEST, Direction.EAST, !isFullBox));
        validAxis.addAll(checkAxis(eyePos.y - blockCenter.y, Direction.DOWN, Direction.UP, true));
        validAxis.addAll(checkAxis(eyePos.z - blockCenter.z, Direction.NORTH, Direction.SOUTH, !isFullBox));
        return validAxis.contains(side);
    }

    public static ArrayList<Direction> checkAxis(double diff, Direction negativeSide, Direction positiveSide, boolean bothIfInRange) {
        ArrayList<Direction> valid = new ArrayList<>();
        if (diff < -0.5) {
            valid.add(negativeSide);
        }
        if (diff > 0.5) {
            valid.add(positiveSide);
        }
        if (bothIfInRange) {
            if (!valid.contains(negativeSide)) valid.add(negativeSide);
            if (!valid.contains(positiveSide)) valid.add(positiveSide);
        }
        return valid;
    }

    public static boolean canSee(BlockPos pos, Direction side) {
        Vec3d testVec = pos.toCenterPos().add(side.getVector().getX() * 0.5, side.getVector().getY() * 0.5, side.getVector().getZ() * 0.5);
        HitResult result = mc.world.raycast(new RaycastContext(getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        return result == null || result.getType() == HitResult.Type.MISS;
    }


}
