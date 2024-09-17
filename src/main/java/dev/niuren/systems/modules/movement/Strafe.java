package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.MoveEvent;
import dev.niuren.events.event.PacketEvent;
import dev.niuren.events.event.PlayerMoveEvent;
import dev.niuren.ic.Command;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.player.MovementUtil;
import dev.niuren.utils.timers.TimerUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

/**
 * @author NiuRen0827
 * Time:20:02
 */
@Module.Info(name = "Strafe", description = "Strafe", category = Module.Category.Movement, chineseName = "灵活移动")
public class Strafe extends Module {
    private final Setting<Boolean> jump =
        register("Jump", true);
    private final Setting<Boolean> inWater =
        register("InWater", false);
    private final Setting<Double> strafeSpeed =
        register("StrafeSpeed", 287, 100.0, 1000.0, 1);
    private final Setting<Double> strafeY =
        register("StrafeY", 0.99f, 0.1f, 1.2f, 1);
    private final Setting<Boolean> explosions =
        register("Explosions", false);
    private final Setting<Boolean> velocity =
        register("Velocity", false);
    private final Setting<Double> multiplier =
        register("H-Factor", 1.0f, 0.0f, 5.0f, 1);
    private final Setting<Double> vertical =
        register("V-Factor", 1.0f, 0.0f, 5.0f, 1);
    private final Setting<Double> coolDown =
        register("CoolDown", 1000, 0, 5000, 1);
    private final Setting<Double> lagTime =
        register("LagTime", 500, 0, 1000, 1);
    private final Setting<Double> cap =
        register("Cap", 10.0, 0.0, 10.0, 1);
    private final Setting<Boolean> scaleCap =
        register("ScaleCap", false);
    private final Setting<Boolean> slow =
        register("Slowness", false);
    private final Setting<Boolean> debug =
        register("Debug", false);
    private final TimerUtils expTimer = new TimerUtils();
    private final TimerUtils lagTimer = new TimerUtils();
    private boolean stop;
    private double speed;
    private double distance;

    private int stage;
    private double lastExp;
    private boolean boost;

    @Override
    public void onActivate() {
        if (mc.player != null) {
            speed = MovementUtil.getSpeed(false);
            distance = MovementUtil.getDistance2D();
        }

        stage = 4;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void invoke(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.packet instanceof EntityVelocityUpdateS2CPacket packet) {
            if (mc.player != null
                && packet.getId() == mc.player.getId()
                && this.velocity.get()) {
                double speed = Math.sqrt(
                    packet.getVelocityX() * packet.getVelocityX()
                        + packet.getVelocityZ() * packet.getVelocityZ())
                    / 8000.0;

                this.lastExp = this.expTimer
                    .passedMillis(this.coolDown.get().longValue())
                    ? speed
                    : (speed - this.lastExp);

                if (this.lastExp > 0) {
                    this.expTimer.reset();
                    mc.executeTask(() ->
                    {
                        this.speed +=
                            this.lastExp * this.multiplier.get();

                        this.distance +=
                            this.lastExp * this.multiplier.get();

                        if (MovementUtil.getMotionY() > 0
                            && this.vertical.get() != 0) {
                            MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.get());
                        }
                    });
                }
            }
        } else if (event.packet instanceof PlayerPositionLookS2CPacket) {
            lagTimer.reset();
            if (mc.player != null) {
                this.distance = 0.0;
            }

            this.speed = 0.0;
            this.stage = 4;
        } else if (event.packet instanceof ExplosionS2CPacket packet) {

            if (this.explosions.get()
                && MovementUtil.isMoving()) {
                if (mc.player.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) < 200) {
                    double speed = Math.sqrt(
                        Math.abs(packet.getPlayerVelocityX() * packet.getPlayerVelocityX())
                            + Math.abs(packet.getPlayerVelocityZ() * packet.getPlayerVelocityZ()));
                    if (debug.get()) Command.sendClientMessage("speed:" + speed + " lastExp:" + lastExp);
                    this.lastExp = this.expTimer
                        .passedMillis(this.coolDown.get().longValue())
                        ? speed
                        : (speed - this.lastExp);

                    if (this.lastExp > 0) {
                        if (debug.get()) Command.sendClientMessage("boost");
                        this.expTimer.reset();

                        this.speed +=
                            this.lastExp * this.multiplier.get();

                        this.distance +=
                            this.lastExp * this.multiplier.get();

                        if (MovementUtil.getMotionY() > 0) {
                            MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.get());
                        }
                    } else {
                        if (debug.get()) Command.sendClientMessage("failed boost");
                    }
                }
            }
        }
    }

    @EventHandler
    public void invoke(PlayerMoveEvent event) {
        if (nullCheck()) return;
        if (!MovementUtil.isMoving()) {
            MovementUtil.setMotionX(0);
            MovementUtil.setMotionZ(0);
        }
        this.distance = MovementUtil.getDistance2D();
    }

    @EventHandler
    public void invoke(MoveEvent event) {
        if (!this.inWater.get()
            && (mc.player.isTouchingWater())
            || mc.player.isHoldingOntoLadder()) {
            this.stop = true;
            return;
        }

        if (this.stop) {
            this.stop = false;
            return;
        }

        move(event);
    }

    private void move(MoveEvent event) {
        if (!MovementUtil.isMoving()) {
            return;
        }

        if (mc.player.checkFallFlying()) return;
        if (!lagTimer.passedMillis(this.lagTime.get().longValue())) {
            return;
        }

        if (this.stage == 1 && MovementUtil.isMoving()) {
            this.speed = 1.35 * MovementUtil.getSpeed(this.slow.get(), this.strafeSpeed.get() / 1000) - 0.01;
        } else if (this.stage == 2 && mc.player.isOnGround() && MovementUtil.isMoving() && (MovementUtil.isJumping() || this.jump.get())) {
            double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
            MovementUtil.setMotionY(yMotion);
            event.setY(yMotion);
            this.speed = this.speed * (this.boost ? 1.6835 : 1.395);
        } else if (this.stage == 3) {
            this.speed = this.distance - 0.66
                * (this.distance - MovementUtil.getSpeed(this.slow.get(), this.strafeSpeed.get() / 1000));

            this.boost = !this.boost;
        } else {
            if ((mc.world.canCollide(null,
                mc.player
                    .getBoundingBox()
                    .offset(0.0, MovementUtil.getMotionY(), 0.0))
                || mc.player.collidedSoftly)
                && this.stage > 0) {
                this.stage = MovementUtil.isMoving() ? 1 : 0;
            }

            this.speed = this.distance - this.distance / 159.0;
        }

        this.speed = Math.min(this.speed, this.getCap());
        this.speed = Math.max(this.speed, MovementUtil.getSpeed(this.slow.get(), this.strafeSpeed.get() / 1000));
        double n = MovementUtil.getMoveForward();
        double n2 = MovementUtil.getMoveStrafe();
        double n3 = mc.player.getYaw();
        if (n == 0.0 && n2 == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (n != 0.0 && n2 != 0.0) {
            n *= Math.sin(0.7853981633974483);
            n2 *= Math.cos(0.7853981633974483);
        }
        double n4 = this.strafeY.get();
        event.setX((n * this.speed * -Math.sin(Math.toRadians(n3)) + n2 * this.speed * Math.cos(Math.toRadians(n3))) * n4);
        event.setZ((n * this.speed * Math.cos(Math.toRadians(n3)) - n2 * this.speed * -Math.sin(Math.toRadians(n3))) * n4);

        if (MovementUtil.isMoving()) {
            this.stage++;
        }
    }

    public double getCap() {
        double ret = cap.get();

        if (!scaleCap.get()) {
            return ret;
        }

        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffects.SPEED)
                .getAmplifier();

            ret *= 1.0 + 0.2 * (amplifier + 1);
        }

        if (slow.get() && mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffects.SLOWNESS)
                .getAmplifier();

            ret /= 1.0 + 0.2 * (amplifier + 1);
        }

        return ret;
    }
}
