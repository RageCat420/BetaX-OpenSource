package dev.niuren.systems.modules.render;

import dev.niuren.events.event.Render3DEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.awt.*;

import static dev.niuren.utils.block.BlockUtil.getBlock;
import static dev.niuren.utils.block.BlockUtil.getState;

@Module.Info(name = "HoleESP", category = Module.Category.Render, description = "HoleESP", chineseName = "坑显示")

public class HoleESP extends Module {
    public HoleESP(){
        super();
    }
    private final Setting<Integer> range
        = register("Range", 5, 0, 20);
    private final Setting<Integer> yrange
        = register("YRange", 5, 0, 20);


    private final Setting<Boolean> obiHole
        = register("obiHole", true);
    private final Setting<Double> obiHoleH
        = register("obiHoleH", 0.8f, 0.0f, 1.0f);
    private final Setting<Integer> obiHoleR
        = register("obiHoleR", 255, 0, 255);
    private final Setting<Integer> obiHoleG
        = register("obiHoleG", 255, 0, 255);
    private final Setting<Integer> obiHoleB
        = register("obiHoleB", 255, 0, 255);
    private final Setting<Integer> obiHoleA
        = register("obiHoleA", 100, 0, 255);


    private final Setting<Boolean> brHole
        = register("brHole", true);
    private final Setting<Double> brHoleH
        = register("brHoleH", 0.8f, 0.0f, 1.0f);

    private final Setting<Integer> brHoleR
        = register("brHoleR", 255, 0, 255);
    private final Setting<Integer> brHoleG
        = register("brHoleG", 255, 0, 255);
    private final Setting<Integer> brHoleB
        = register("brHoleB", 255, 0, 255);
    private final Setting<Integer> brHoleA
        = register("brHoleA", 100, 0, 255);



    private final Setting<Boolean> otherHole
        = register("otherHole", true);
    private final Setting<Double> otherHoleH
        = register("otherHoleH", 0.8f, 0.0f, 1.0f);

    private final Setting<Integer> otherHoleR
        = register("otherHoleR", 255, 0, 255);
    private final Setting<Integer> otherHoleG
        = register("otherHoleG", 255, 0, 255);
    private final Setting<Integer> otherHoleB
        = register("otherHoleB", 255, 0, 255);
    private final Setting<Integer> otherHoleA
        = register("otherHoleA", 100, 0, 255);
    @EventHandler
    public void onRender3D(Render3DEvent event){
        if (nullCheck()) {
            return;
        }
        BlockPos playerPos = mc.player.getBlockPos();
        for (int x = playerPos.getX() - range.get(); x < playerPos.getX() + range.get(); ++x) {
            for (int z = playerPos.getZ() - range.get(); z < playerPos.getZ() + range.get(); ++z){
                for (int y = playerPos.getY() + yrange.get(); y > playerPos.getY() - yrange.get(); --y){
                    BlockPos pos = new BlockPos(x, y, z);
                    if(isHole(pos, true, true, Blocks.OBSIDIAN, false)){
                        if(obiHole.get()) {
                            Render3DUtils.drawHole(event.matrix(), pos, new Color(obiHoleR.get(), obiHoleG.get(), obiHoleB.get(),obiHoleA.get()), true, true, obiHoleH.get(), 2);
                        }
                    }
                    else if(isHole(pos, true, true, Blocks.BEDROCK, false)){
                        if(brHole.get()) {
                            Render3DUtils.drawHole(event.matrix(), pos, new Color(brHoleR.get(), brHoleG.get(), brHoleB.get(), brHoleA.get()), true, true, brHoleH.get(), 2);
                        }
                    }
                    else if(isHole(pos, true, true, Blocks.BEDROCK, true)){
                        if(otherHole.get()) {
                            Render3DUtils.drawHole(event.matrix(), pos, new Color(otherHoleR.get(), otherHoleG.get(), otherHoleB.get(), otherHoleA.get()), true, true, otherHoleH.get(), 2);
                        }
                    }
                }
            }
        }
    }
    public static boolean isHard(BlockPos pos) {
        Block block = getState(pos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.NETHERITE_BLOCK || block == Blocks.ENDER_CHEST || block == Blocks.BEDROCK || block == Blocks.ANVIL;
    }
    public static boolean isHole(BlockPos pos, boolean canStand, boolean checkTrap, Block block, boolean hard) {
        int blockProgress = 0;
        for (Direction i : Direction.values()) {
            if (i == Direction.UP || i == Direction.DOWN) continue;
            if (isHard(pos.offset(i)) && hard || getBlock(pos.offset(i)) == block)
                blockProgress++;
        }
        return
            (
                !checkTrap || (getBlock(pos) == Blocks.AIR
                    && getBlock(pos.add(0, 1, 0)) == Blocks.AIR
                    && getBlock(pos.add(0, 2, 0)) == Blocks.AIR)
            )
                && blockProgress > 3
                && (!canStand || getState(pos.add(0, -1, 0)).blocksMovement());
    }
}
