package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.*;
import dev.niuren.ic.Friends;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.StrafeFix;
import dev.niuren.utils.block.BlockUtil;
import dev.niuren.utils.damage.ExplosionUtil;
import dev.niuren.utils.damage.OyveyExplosionUtil;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.player.ExtrapolationUtils;
import dev.niuren.utils.player.InventoryUtils;
import dev.niuren.utils.player.MovementUtil;
import dev.niuren.utils.render.AnimateUtil;
import dev.niuren.utils.render.ColorUtil;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

import java.awt.*;
import java.util.*;

import static dev.niuren.utils.block.BlockUtil.*;
import static dev.niuren.utils.block.BlockUtils.getLegitRotations;
import static dev.niuren.utils.block.BlockUtils.getWorldActionId;


/**
 * @author NiuRen0827
 * Time:13:28
 */
@Module.Info(name = "AutoCrystal", chineseName = "自动水晶", category = Module.Category.Combat, description = "Automatically place and attack crystals.")
public class AutoCrystal extends Module {
    public AutoCrystal() {
        if (bestTarget != null) {
            displayInfo = String.valueOf(bestTarget.getName());
        }
        INSTNACE = this;
    }
    public static AutoCrystal get() {
        return INSTNACE;
    }
    public static AutoCrystal INSTNACE;
    // General
    private final Setting<Boolean> ignoreTerrain
        = register("IgnoreTerrain", true);
    private final Setting<Double> wallRange
        = register("WallRange", 5.0, 0, 6);
    private final Setting<Double> entitiesRange
        = register("Entities", 6.0, 1.0, 12);
    private final Setting<Integer> hurtTime
        = register("HurtTime", 10, 0, 10);
    private final Setting<Boolean> swing
        = register("SwingHand", true);
    private final Setting<Boolean> serverSwing
        = register("ServerSwing", true);
    private final Setting<Integer> selfExt
        = register("SelfExtrapolation", 0, 0, 20);
    private final Setting<Integer> extrapolation
        = register("Extrapolation", 0, 0, 20);
    private final Setting<Integer> extSmoothness
        = register("ExtrapolationSmoothening", 2, 1, 20);
    private final Setting<Boolean> sync
        = register("Sync", true);
    private final Setting<Boolean> motionSync
        = register("MotionSync", true);
    private final Setting<Boolean> crystalSync
        = register("CrystalSync", false);
    private final Setting<Boolean> obsPlace
        = register("ObsidianPlace", true);
    private final Setting<Boolean> antiSuicide
        = register("AntiSuicide", true);
    private final Setting<Boolean> ignoreWall
        = register("IgnoreWall", true);
    private final Setting<Boolean> silent
        = register("SilentSwap", true);
    private final Setting<Boolean> usingPause
        = register("UsingPause", true);
    // Interact
    private final Setting<Boolean> placeCrystal
        = register("PlaceCrystal", true);
    private final Setting<Double> placeRange
        = register("PlaceRange", 6.0, 0, 12);
    private final Setting<Double> radius
        = register("Radius", 3.0, 0, 6);
    private final Setting<Double> placeMin
        = register("PlaceMin", 6.0, 0, 36);
    private final Setting<Double> placeDelay
        = register("PlaceDelay", 0.0, 0.0, 100.0);
    private final Setting<Boolean> breakCrystal
        = register("BreakCrystal", true);
    private final Setting<Double> breakMax
        = register("BreakMax", 8.0, 0, 36);
    private final Setting<Double> breakRange
        = register("BreakRange", 6.0, 0, 6);
    private final Setting<Double> breakDelay
        = register("BreakDelay", 0.0, 0.0, 100.0);
    private final Setting<Boolean> removeCrystal
        = register("RemoveCrystal", false);
    private final Setting<Boolean> lock
        = register("PlaceLock", false);
    // Rotate
    private final Setting<Boolean> rotate
        = register("Rotate", true);
    // Render
    private final Setting<Boolean> render
        = register("Render", true);
    private final Setting<Boolean> old
        = register("Old", true);
    private final Setting<Boolean> shrink
        = register("Shrink", true);
    private final Setting<Boolean> box
        = register("Box", true);
    private final Setting<Integer> bred
        = register("BoxRed", 255, 0, 255);
    private final Setting<Integer> bgreen
        = register("BoxGreen", 255, 0, 255);
    private final Setting<Integer> bblue
        = register("BoxBlue", 255, 0, 255);
    private final Setting<Integer> boxalpha
        = register("Boxalpha", 255, 0, 255);
    private final Setting<Boolean> fill
        = register("FillBox", true);
    private final Setting<Integer> fred
        = register("FillRed", 255, 0, 255);
    private final Setting<Integer> fgreen
        = register("FillGreen", 255, 0, 255);
    private final Setting<Integer> fblue
        = register("FillBlue", 255, 0, 255);
    private final Setting<Integer> fillalpha
        = register("FillAlpha", 90, 0, 255);
    private final Setting<Boolean> bold
        = register("Bold", true);
    private final Setting<Integer> lineWidth
        = register("LineWidth", 90, 0, 255);
    private final Setting<Double> fadeSpeed
        = register("FadeSpeed", 1.0, 0.0, 1.0);
    private final Setting<Double> startFadeTime
        = register("StartFadeSpeed", 0.3d, 0d, 2d);
    private final Setting<Double> sliderSpeed
        = register("SliderSpeed", 0.2, 0.0, 1.0);
    private final Setting<Boolean> renderDamage
        = register("RenderDamage", true);
    private final Setting<Integer> red
        = register("TextRed", 255, 0, 255);
    private final Setting<Integer> green
        = register("TextGreen", 255, 0, 255);
    private final Setting<Integer> blue
        = register("TextBlue", 255, 0, 255);
    private final Setting<Boolean> renderExt
        = register("RenderExtrapolation", false);
    private final Setting<Boolean> renderSelfExt
        = register("RenderSelfExtrapolation", false);

    private BlockPos blockPos = null;
    private BlockPos obsPos = null;
    private float lastYaw = 0f;
    private float lastPitch = 0f;
    private final ArrayList<BlockPos> crystalPos = new ArrayList<>();
    private Map<AbstractClientPlayerEntity, Box> extPos = new HashMap<>();
    private PlayerEntity bestTarget = null;
    private boolean placing = false;
    private boolean breaking = false;
    float lastDamage = 0;
    final Timer noPosTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer breakCrystalTimer = new Timer();
    static Vec3d placeVec3d;
    static Vec3d curVec3d;
    double fade = 0;
    @Override
    public void onActivate() {
        extPos.clear();
    }

    private boolean validForIntersect(Entity entity) {
        if (entity instanceof EndCrystalEntity) {
            return false;
        }

        return !(entity instanceof PlayerEntity) || !entity.isSpectator();
    }

    @EventHandler
    public void onMotionUpdate(MotionEvent event) {
        if (nullCheck()) return;

        if (motionSync.get()) {
            update();
        }

        double op = Double.MIN_VALUE;
        BlockPos maxDamagePos;
        /* 这里计算Damage */
        for (BlockPos pos : crystalPos) {
            if (bestTarget == null) return;
            double damage = getDmg(pos);
            if (damage > op && canPlace(pos, placeRange.get())) {
                op = damage;
                maxDamagePos = pos;
                if (behindWall(maxDamagePos) && !ignoreWall.get()) return;

                this.blockPos = maxDamagePos;
                this.obsPos = blockPos.add(0, -1, 0);
            }
        }
        if (blockPos != null) {
            /* 转头 */
            StrafeFix strafeFix = Modules.get().get(StrafeFix.class);
            if (rotate.get() && strafeFix.toggled && canPlace(blockPos, placeRange.get())) {
                /* 判断 */
                float[] angle = getLegitRotations(blockPos.toCenterPos());
                float[] angle2 = getLegitRotations(obsPos.toCenterPos());
                if (placing) {
                    strafeFix.setRotation(angle2[0], angle2[1]);
                }
                if (breaking) {
                    strafeFix.setRotation(angle[0], angle[1]);
                }
                rotateReset();
            } else if (rotate.get() && canPlace(blockPos, placeRange.get())) {
                float[] angle = getLegitRotations(blockPos.toCenterPos());
                float[] angle2 = getLegitRotations(obsPos.toCenterPos());
                if (breaking) {
                    event.setYaw(angle2[0]);
                    event.setPitch(angle2[1]);
                }
                if (placing) {
                    event.setYaw(angle[0]);
                    event.setPitch(angle[1]);
                }
                rotateReset();
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (nullCheck()) {
            return;
        }
        ExtrapolationUtils.extrapolateMap(extPos, player -> player == mc.player ? selfExt.get() : extrapolation.get(), player -> extSmoothness.get());
        update();
    }

    private void update() {
        if (antiSuicide.get()) {
            if (mc.player.getHealth() <= 2) {
                return;
            }
        }
        if (usingPause.get() && mc.player.isUsingItem()) {
            return;
        }
        crystalPos.clear();

        for (PlayerEntity target : EntitiesUtil.getEnemies(entitiesRange.get())) {
            for (BlockPos pos : BlockUtil.getSphere(radius.get().floatValue(), target.getBlockPos()))
                if (!target.isDead()) {
                    if (BlockUtil.getBlock(pos.add(0, -1, 0)) == Blocks.OBSIDIAN
                        || BlockUtil.getBlock(pos.add(0, -1, 0)) == Blocks.BEDROCK) {
                        float damage = (float) getDmg(pos);
                            // OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), target, target, 6f);
                        if (damage > placeMin.get()) {
                            lastDamage = damage;
                            if (antiSuicide.get()) {
                                if (OyveyExplosionUtil.calculateDamage(pos.getX(), pos.getY(), pos.getZ(), mc.player, mc.player, 6f) <= breakMax.get()) {
                                    if (canPlace(pos, placeRange.get())) {
                                        crystalPos.add(pos);
                                        bestTarget = target;
                                    }
                                }
                            } else {
                                if (canPlace(pos, placeRange.get())) {
                                    crystalPos.add(pos);
                                    bestTarget = target;
                                }
                            }
                        }
                   //     extPos.put((AbstractClientPlayerEntity) target, ExtrapolationUtils.extrapolate((AbstractClientPlayerEntity) target, 2, 1));
                    }
                }else{
                    extPos.clear();
                }
        }
        if (bestTarget != null && mc.player.distanceTo(bestTarget) <= entitiesRange.get() && !Friends.get().isFriend(bestTarget)){
            doCrystal();
        }

    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        if(nullCheck()||obsPos==null||blockPos == null)return;
        if (!render.get()) return;
        if (!canPlace(blockPos, placeRange.get())) return;
        if (crystalPos != null) {
            noPosTimer.reset();
            placeVec3d = obsPos.toCenterPos();
        }
        if (placeVec3d == null) {
            return;
        }
        if (fadeSpeed.get() >= 1) {
            fade = noPosTimer.passedMs((long) (startFadeTime.get() * 1000)) ? 0 : 0.5;
        } else {
            fade = AnimateUtil.animate(fade, noPosTimer.passedMs((long) (startFadeTime.get() * 1000)) ? 0 : 0.5, fadeSpeed.get() / 10);
        }
        if (fade == 0) {
            curVec3d = null;
            return;
        }
        if (curVec3d == null || sliderSpeed.get() >= 1) {
            curVec3d = placeVec3d;
        } else {
            curVec3d = new Vec3d(AnimateUtil.animate(curVec3d.x, placeVec3d.x, sliderSpeed.get() / 10),
                AnimateUtil.animate(curVec3d.y, placeVec3d.y, sliderSpeed.get() / 10),
                AnimateUtil.animate(curVec3d.z, placeVec3d.z, sliderSpeed.get() / 10));
        }


        //水晶渲染
        Box cbox = new Box(curVec3d, curVec3d);
        if (shrink.get()) {
            cbox = cbox.expand(fade);
        } else {
            cbox = cbox.expand(0.5);
        }
        MatrixStack matrixStack = event.getMatrixStack();
        if (fill.get()) {
            Render3DUtils.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(new Color(fred.get(),fblue.get(),fgreen.get()), (int) (fillalpha.get() * fade * 2D)));
        }
        if (box.get()) {
            if (!bold.get()) {
                Render3DUtils.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(new Color(bred.get(),bblue.get(),bgreen.get()), (int) (boxalpha.get() * fade * 2D)));
            } else {
                Render3DUtils.drawLine(cbox, ColorUtil.injectAlpha(new Color(bred.get(),bblue.get(),bgreen.get()), (int) (boxalpha.get() * fade * 2D)), lineWidth.getInc());

            }
        }


        if(old.get()) {
            if (blockPos != null && canPlace(blockPos, placeRange.get())) {
                event.draw3DBox(event.matrix(), new Box(obsPos), new Color(0xFFFFFF), true, false, 2);
                if (renderDamage.get()) {
                    Render3DUtils.drawText3D(String.format("%.2f", lastDamage), BlockPos.ofFloored(new Vec3d(placeVec3d.toVector3f())), new Color(red.get(), green.get(), blue.get()));
                }
            }
        }
        // Render extrapolation
        if (renderExt.get()) {
            if (extPos != null) {
                extPos.forEach((name, bb) -> {
                    if (renderSelfExt.get() || !name.equals(mc.player)) {
                        event.draw3DBox(event.matrix(), bb, new Color(255, 255, 255), true, true, 1);
                    }
                });
            }
        }
    }



    @Override
    public void onDeactivate() {
        blockPos = null;
        bestTarget = null;
        lastYaw = MovementUtil.lastYaw;
        lastPitch = MovementUtil.lastPitch;
    }

    @EventHandler
    public void onLeftGame(GameLeftEvent event) {
        blockPos = null;
        bestTarget = null;
        lastYaw = MovementUtil.lastYaw;
        lastPitch = MovementUtil.lastPitch;
    }
    private double getDmg(BlockPos blockPos) {
        double highest = -1.0;
        for (Map.Entry<AbstractClientPlayerEntity, Box> entry : extPos.entrySet()) {
            AbstractClientPlayerEntity player = entry.getKey();
            if (player.getHealth() <= 0 || player == mc.player) continue;

            Box box = entry.getValue();
            double dmg = ExplosionUtil.crystalDamage(player, box, blockPos.toCenterPos(), obsPos, ignoreTerrain.get());

            if (dmg > highest) {
                highest = dmg;
            }
        }

        return highest;
    }

    public boolean behindWall(BlockPos pos) {
        Vec3d testVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 2 * 0.85, pos.getZ() + 0.5);
        HitResult result = mc.world.raycast(new RaycastContext(BlockUtil.getEyesPos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        if (result == null || result.getType() == HitResult.Type.MISS) return false;
        return MathHelper.sqrt((float) BlockUtil.getEyesPos().squaredDistanceTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)) > wallRange.get();
    }

    private boolean canPlace(BlockPos pos, double distance) {
        if (MathHelper.sqrt((float) mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) > distance)
            return false;
        if (!canBlockFacing(pos)) return false;
        if (!canReplace(pos)) return false;
        return !hasEntityBlockCrystal(pos, true, false) && (getBlock(pos.down()) == Blocks.OBSIDIAN || getBlock(pos.down()) == Blocks.BEDROCK);
    }

    public boolean hasEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
            if (!entity.isAlive() || ignoreItem && entity instanceof ItemEntity || entity instanceof ArmorStandEntity)
                continue;
            if (entity instanceof EndCrystalEntity) {
                if (!ignoreCrystal) return true;
                if (mc.player.canSee(entity) || mc.player.getEyePos().distanceTo(entity.getPos()) <= wallRange.get()) {
                    continue;
                }
            }
            return true;
        }
        return false;
    }

    private float[] injectStep(float[] angle, float steps) {
        if (steps < 0.01f) steps = 0.01f;

        if (steps > 1) steps = 1;

        if (steps < 1 && angle != null) {
            float packetYaw = lastYaw;
            float diff = MathHelper.wrapDegrees(angle[0] - packetYaw);

            if (Math.abs(diff) > 90 * steps) {
                angle[0] = (packetYaw + (diff * ((90 * steps) / Math.abs(diff))));
            }

            float packetPitch = lastPitch;
            diff = angle[1] - packetPitch;
            if (Math.abs(diff) > 90 * steps) {
                angle[1] = (packetPitch + (diff * ((90 * steps) / Math.abs(diff))));
            }
        }

        return new float[]{
            angle[0],
            angle[1]
        };
    }

    private void doCrystal() {
        if (blockPos == null) return;
        if (!canPlace(blockPos, placeRange.get())) return;
        if (placeCrystal.get()) {
            placeCrystal(lock.get() ? Direction.UP : getClickSide(obsPos));
            placing = true;
        }
        // 这里使用placeRange来判断是否可以放置水晶
        if (breakCrystal.get() && canPlace(blockPos, placeRange.get())) {
            breakCrystal();
            breaking = true;
        }
        if (crystalSync.get() && !(mc.player.handSwingTicks > hurtTime.get())) {
            crystalSync();
            breaking = true;
        }
    }

    private void placeCrystal(Direction direction) {
        int crystal = InventoryUtils.findItem(Items.END_CRYSTAL);
        int old = mc.player.getInventory().selectedSlot;
        BlockPos supportPos = blockPos.add(0, -1, 0);
        Vec3d directionVec
            = new Vec3d(supportPos.getX()
            + 0.5
            + direction.getVector().getX() * 0.5, supportPos.getY()
            + 0.5 + direction.getVector().getY() * 0.5, supportPos.getZ()
            + 0.5
            + direction.getVector().getZ() * 0.5);
        if (crystal == -1) {
            return;
        }
        if (behindWall(blockPos) && !ignoreWall.get()) {
            return;
        }
        InventoryUtils.doSwap(crystal);
        if (placeTimer.passed(placeDelay.get())){
            mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(directionVec, direction, supportPos, false), getWorldActionId(mc.world)));
            if (silent.get()) {
                InventoryUtils.doSwap(old);
            }
            placeTimer.reset();
        }

    }

    private void crystalSync() {
        for (Entity target : mc.world.getEntities()) {
            if (target instanceof EndCrystalEntity) {
                if (mc.player.distanceTo(target) < breakRange.get()) {
                    mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                    if (sync.get()) {
                        placeCrystal(getClickSide(blockPos));
                    }
                    if (swing.get()) {
                        if (serverSwing.get()) {
                            mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        } else {
                            mc.player.swingHand(Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }

    public void breakCrystal() {
        Hand hand = Hand.MAIN_HAND;
        Iterator<EndCrystalEntity> endCrystalEntityIterator = mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box(blockPos.getX(), blockPos.getY(), (double) blockPos.getZ(), (double) (blockPos.getX() + 1), (double) (blockPos.getY() + 2), (double) (blockPos.getZ() + 1))).iterator();
        if (endCrystalEntityIterator.hasNext()) {
            EndCrystalEntity crystal = endCrystalEntityIterator.next();
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
            if (sync.get()) {
                placeCrystal(getClickSide(blockPos));
            }
            if (breakCrystalTimer.passed(breakDelay.get())){
                if (removeCrystal.get()) {
                    mc.world.removeEntity(crystal.getId(), Entity.RemovalReason.KILLED);
                } else mc.player.attack(crystal);
                boolean shouldRenderSwing = swing.get();
                if (shouldRenderSwing) {
                    mc.player.swingHand(hand);
                } else {
                    mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
                }
                breakCrystalTimer.reset();
            }

        }
    }
    private void rotateReset() {
        placing = false;
        breaking = false;
    }
}
