package dev.niuren.systems.modules.combat;


import dev.niuren.events.event.Render3DEvent;
import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.Task;
import dev.niuren.utils.block.BlockUtil;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.player.InventoryUtil;
import dev.niuren.utils.player.InventoryUtils;
import dev.niuren.utils.player.PlayerUtils;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.niuren.utils.block.BlockUtil.isAir;
import static dev.niuren.utils.block.BlockUtil.isCombatBlock;


@Module.Info(name = "CityMiner", chineseName = "自动挖角", category = Module.Category.Combat, description = "")

public class CityMiner extends Module {

    private final Setting<Boolean> chatInfo
        = register("chatInfo", true);
    private final Setting<Double> entitiesRange
        = register("Entities", 5.0, 0.0, 8.0);
    private final Setting<Boolean> usingPause
        = register("UsingPause", true);
    private final Setting<Boolean> hotBar
        = register("HotbarSwap", true);


    public CityMiner() {
        super();
    }

    private BlockPos breakPos = null;
    private PlayerEntity target;

    private final Task crystalTask = new Task();
    private final Task supportTask = new Task();
    private PlayerEntity bestTarget = null;
    private final Timer mineTimer = new Timer();
    public static double progress = 0;

    @Override
    public void onActivate() {
        crystalTask.reset();
        supportTask.reset();

        breakPos = null;
    }

    @Override
    public void onDeactivate() {
        if (breakPos != null) mc.interactionManager.attackBlock(breakPos, Direction.UP);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) {
            return;
        }
        if (usingPause.get() && mc.player.isUsingItem()) {
            return;
        }
        update();
        target = bestTarget;

        int pickaxe = InventoryUtils.findItem(Items.NETHERITE_PICKAXE);

        if (pickaxe==0) {
            toggle();
            return;
        }

        if (breakPos == null) breakPos = getBreakPos(target);
        if (breakPos == null || isAir(breakPos)) {
            if (breakPos == null);
            toggle();
            return;
        }


        mc.interactionManager.updateBlockBreakingProgress(breakPos, Direction.UP);
        int old = mc.player.getInventory().selectedSlot;
        if(progress>=99) {
            InventoryUtils.doSwap(pickaxe);
        }
    }
    public final double getBreakTime(BlockPos pos, int slot) {
        return getBreakTime(pos, slot, 0.7f);
    }
    public final double getBreakTime(BlockPos pos, int slot, double damage) {
        return (1 / getBlockStrength(pos, mc.player.getInventory().getStack(slot)) / 20 * 1000 * damage);
    }
    public float getBlockStrength(BlockPos position, ItemStack itemStack) {
        BlockState state = mc.world.getBlockState(position);
        float hardness = state.getHardness(mc.world, position);
        if (hardness < 0) {
            return 0;
        }
        if (!canBreak(position)) {
            return getDigSpeed(state, itemStack) / hardness / 100F;
        } else {
            return getDigSpeed(state, itemStack) / hardness / 30F;
        }
    }
    public float getDigSpeed(BlockState state, ItemStack itemStack) {
        float digSpeed = getDestroySpeed(state, itemStack);
        if (digSpeed > 1) {
            int efficiencyModifier = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
            if (efficiencyModifier > 0 && !itemStack.isEmpty()) {
                digSpeed += StrictMath.pow(efficiencyModifier, 2) + 1;
            }
        }
        if (mc.player.hasStatusEffect(StatusEffects.HASTE)) {
            digSpeed *= 1 + (mc.player.getStatusEffect(StatusEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }
        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float fatigueScale;
            switch (mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> fatigueScale = 0.3F;
                case 1 -> fatigueScale = 0.09F;
                case 2 -> fatigueScale = 0.0027F;
                default -> fatigueScale = 8.1E-4F;
            }
            digSpeed *= fatigueScale;
        }
        if (mc.player.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(mc.player)) {
            digSpeed /= 5;
        }
        if (!mc.player.isOnGround()) {
            digSpeed /= 5;
        }
        return (digSpeed < 0 ? 0 : digSpeed);
    }
    public float getDestroySpeed(BlockState state, ItemStack itemStack) {
        float destroySpeed = 1;
        if (itemStack != null && !itemStack.isEmpty()) {
            destroySpeed *= itemStack.getMiningSpeedMultiplier(state);
        }
        return destroySpeed;
    }
    private boolean canBreak(BlockPos pos) {
        final BlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getHardness() != -1;
    }
    private void update(){
        for (PlayerEntity target : EntitiesUtil.getEnemies(entitiesRange.get())) {
            if (!target.isDead()) {
                bestTarget=target;
            }
        }
    }
    static DecimalFormat df = new DecimalFormat("0.0");
    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) {
            return;
        }
        if(breakPos!=null) {
            Box box = new Box(breakPos);
            int slot = getTool(breakPos);
            if (slot == -1) {
                slot = mc.player.getInventory().selectedSlot;
            }
            double breakTime = getBreakTime(breakPos, slot);
            event.draw3DBox(event.matrix(), box, new Color(255, 42, 42, 120), true, true, 1);
            if ((int) mineTimer.getPassedTimeMs() < breakTime) {
                Render3DUtils.drawText3D(df.format(progress * 100) + "%", breakPos.toCenterPos(), -1);
            }
        }else{
            progress = 0;
        }
    }
    public static BlockPos getBreakPos(PlayerEntity target) {
        if (target==null) return null;

        ArrayList<BlockPos> blockArray = new ArrayList<>();
        BlockPos tPos = new BlockPos(target.getBlockPos());

        for (Direction dir : Direction.values()) {
            if (dir.equals(Direction.UP) || dir.equals(Direction.DOWN)) continue;

            if (isCombatBlock(tPos.offset(dir))) blockArray.add(tPos.offset(dir));
        }

        if (blockArray.isEmpty()) return null;

        BlockPos prevBP = blockArray.get(0);
        blockArray.removeIf(BlockUtil::isOurSurround);

        if (blockArray.isEmpty()) blockArray.add(prevBP);
        blockArray.sort(Comparator.comparingDouble(PlayerUtils::distanceTo));
        return blockArray.get(0);
    }
    public int getTool(BlockPos pos) {
        if (hotBar.get()) {
            int index = -1;
            float CurrentFastest = 1.0f;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack != ItemStack.EMPTY) {
                    final float digSpeed = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
                    final float destroySpeed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos));
                    if (digSpeed + destroySpeed > CurrentFastest) {
                        CurrentFastest = digSpeed + destroySpeed;
                        index = i;
                    }
                }
            }
            return index;
        } else {
            AtomicInteger slot = new AtomicInteger();
            slot.set(-1);
            float CurrentFastest = 1.0f;
            for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                if (!(entry.getValue().getItem() instanceof AirBlockItem)) {
                    final float digSpeed = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, entry.getValue());
                    final float destroySpeed = entry.getValue().getMiningSpeedMultiplier(mc.world.getBlockState(pos));
                    if (digSpeed + destroySpeed > CurrentFastest) {
                        CurrentFastest = digSpeed + destroySpeed;
                        slot.set(entry.getKey());
                    }
                }
            }
            return slot.get();
        }
    }
}
