package dev.niuren.mixininterface;

import net.minecraft.util.math.Vec3i;

public interface IVec3d {
    void set(double x, double y, double z);

    default void set(Vec3i vec) {
        set(vec.getX(), vec.getY(), vec.getZ());
    }

    void setXZ(double x, double z);

    void setY(double y);
}
