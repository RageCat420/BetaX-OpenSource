package dev.niuren.systems.modules.combat;

import dev.niuren.ic.Friends;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.StrafeFix;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.math.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

import static dev.niuren.utils.block.BlockUtils.getLegitRotations;

/**
 * @author NiuRen0827
 * Time:21:50
 */
@Module.Info(name = "Killaura", category = Module.Category.Combat, description = "Killaura", chineseName = "自动攻击")
public class Killaura extends Module {
    private final Setting<Boolean> rotate = register("Rotate", true);
    private final Setting<Boolean> swing = register("Swing", true);
    private final Setting<Boolean> oneNice = register("1.9+", true);
    public final Setting<String> blockModeSetting = register("BlockMode", Arrays.asList("Fake","Legit"),"Fake");
    private final Setting<Double> range = register("Range", 5, 1, 10, 1);
    private final Setting<Double> cpsLimit = register("CPSLimit", 2, 0, 20, 1);
    public Entity target = null;
    private Vec3d vec3d = null;
    private Timer timer = new Timer();
    private Timer blockTimer = new Timer();


    public static Killaura get() {
        return INSTNACE;
    }
    public static Killaura INSTNACE;

    public Killaura() {
        INSTNACE = this;
    }
    @EventHandler
    public void onMotionUpdate(dev.niuren.events.event.MotionEvent event) {
        if (nullCheck()) return;
        for (PlayerEntity target : EntitiesUtil.getEnemies(range.get())) {
            if (target.equals(mc.player)) continue;
            if (mc.player.distanceTo(target) > range.get()) continue;
            if (!target.isAlive()) continue;
            if (!target.isInvisible()) {
                this.target = target;
                this.vec3d = target.getPos().add(0, 1.5, 0);
                if (!(mc.player.handSwingTicks > cpsLimit.get())) {
                    if(oneNice.get()) {
                        if (timer.passed(950L)) {
                            if (blockModeSetting.get() == "Legit") {
                                if(mc.player.getInventory().getStack(40) != null) {
                                    mc.player.getInventory().getStack(40).use(mc.world, mc.player, Hand.OFF_HAND);
                                }
                            }
                            if (blockTimer.passed(1000L)) {
                                if (Friends.get().isFriend(target)) return;
                                mc.interactionManager.attackEntity(mc.player, target);
                                //  mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                                if (swing.get()) mc.player.swingHand(Hand.MAIN_HAND);
                                blockTimer.reset();
                                timer.reset();
                            }
                        }
                    } else {
                        if (blockModeSetting.get() == "Legit") {
                            if(mc.player.getInventory().getStack(40) != null) {
                                mc.player.getInventory().getStack(40).use(mc.world, mc.player, Hand.OFF_HAND);
                            }
                        }
                        if (blockTimer.passed(1000L)) {
                            if (Friends.get().isFriend(target)) return;
                            mc.interactionManager.attackEntity(mc.player, target);
                            //  mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                            if (swing.get()) mc.player.swingHand(Hand.MAIN_HAND);
                            blockTimer.reset();
                        }
                    }

                }
            }
        }
        StrafeFix strafeFix = Modules.get().get(StrafeFix.class);
        if (rotate.get() && strafeFix.toggled && vec3d != null && target != null && mc.player.distanceTo(target) <= range.get()) {
            float[] angle = getLegitRotations(vec3d);
            strafeFix.setRotation(angle[0], angle[1]);
        } else {
            if (rotate.get() && vec3d != null && target != null && mc.player.distanceTo(target) <= range.get()) {
                float[] angle = getLegitRotations(vec3d);
                event.setYaw(angle[0]);
                event.setPitch(angle[1]);
            }
        }
    }
}
