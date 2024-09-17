package dev.niuren.utils.player;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

import static dev.niuren.systems.modules.Module.mc;

/**
 * @author NiuRen0827
 * Time:19:36
 */
public class MovementUtil {
    public static float lastYaw = 0;
    public static float lastPitch = 0;

    public static boolean isMoving() {
        return mc.player.input.movementForward != 0.0 || mc.player.input.movementSideways != 0.0;
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;

        if (mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            //noinspection ConstantConditions
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (amplifier + 1) * 0.1;
        }

        return defaultSpeed;
    }

    public static boolean isJumping() {
        return mc.options.jumpKey.isPressed();
    }

    public static double getDistance2D() {
        double xDist = mc.player.getX() - mc.player.prevX;
        double zDist = mc.player.getZ() - mc.player.prevZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getMoveForward() {
        return mc.player.input.movementForward;
    }

    public static double getMoveStrafe() {
        return mc.player.input.movementSideways;
    }


    public static double getMotionX() {
        return mc.player.getVelocity().x;
    }

    public static double getMotionY() {
        return mc.player.getVelocity().y;
    }

    public static double getMotionZ() {
        return mc.player.getVelocity().z;
    }

    public static void setMotionX(double x) {
        Vec3d velocity = new Vec3d(x, mc.player.getVelocity().y, mc.player.getVelocity().z);
        mc.player.setVelocity(velocity);
    }

    public static void setMotionY(double y) {
        Vec3d velocity = new Vec3d(mc.player.getVelocity().x, y, mc.player.getVelocity().z);
        mc.player.setVelocity(velocity);
    }

    public static void setMotionZ(double z) {
        Vec3d velocity = new Vec3d(mc.player.getVelocity().x, mc.player.getVelocity().y, z);
        mc.player.setVelocity(velocity);
    }

    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;
        return getSpeed(slowness, defaultSpeed);
    }

    public static double getSpeed(boolean slowness, double defaultSpeed) {
        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffects.SPEED).getAmplifier();

            defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }

        if (slowness && mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffects.SLOWNESS).getAmplifier();

            defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
        }

        if (mc.player.isSneaking()) defaultSpeed /= 5;
        return defaultSpeed;
    }

    public static double[] forward(final double d) {
        float f = mc.player.input.movementForward;
        float f2 = mc.player.input.movementSideways;
        float f3 = mc.player.getYaw();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += ((f > 0.0f) ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += ((f > 0.0f) ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        final double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        final double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        final double d4 = f * d * d3 + f2 * d * d2;
        final double d5 = f * d * d2 - f2 * d * d3;
        return new double[]{d4, d5};
    }

}
