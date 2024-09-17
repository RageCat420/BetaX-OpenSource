package dev.niuren.utils.player;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static dev.niuren.systems.modules.Module.mc;

public class PlayerUtils {
    public static double distanceTo(Entity entity) {
        return distanceTo(entity.getX(), entity.getY(), entity.getZ());
    }

    public static double distanceTo(BlockPos blockPos) {
        return distanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double distanceTo(Vec3d vec3d) {
        return distanceTo(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    public static double distanceTo(double x, double y, double z) {
        return Math.sqrt(squaredDistanceTo(x, y, z));
    }

    public static double squaredDistanceTo(Entity entity) {
        return squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
    }

    public static double squaredDistanceTo(BlockPos blockPos) {
        return squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double squaredDistanceTo(double x, double y, double z) {
        return squaredDistance(mc.player.getX(), mc.player.getY(), mc.player.getZ(), x, y, z);
    }

    public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        float f = (float) (x1 - x2);
        float g = (float) (y1 - y2);
        float h = (float) (z1 - z2);
        return org.joml.Math.fma(f, f, org.joml.Math.fma(g, g, h * h));
    }
}
