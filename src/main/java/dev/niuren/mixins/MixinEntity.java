package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.PlayerMoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static dev.niuren.systems.modules.Module.mc;

/**
 * @author NiuRen0827
 * Time:20:18
 */
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract float getYaw(float tickDelta);

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract int getId();

    @Shadow
    public abstract boolean equals(Object o);

    @Shadow
    public abstract HitResult raycast(double maxDistance, float tickDelta, boolean includeFluids);

    /**
     * @author niuren
     * @reason fix
     */
    @Overwrite
    public void updateVelocity(float speed, Vec3d movementInput) {
        Vec3d vec3d;
        if (mc.player == null || this.getId() == mc.player.getId()) {
            PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(this.getYaw());
            BetaX.EVENT_BUS.post(playerMoveEvent);

            vec3d = movementInputToVelocity(movementInput, speed, playerMoveEvent.getYaw());
        } else vec3d = movementInputToVelocity(movementInput, speed, this.getYaw());
        this.setVelocity(this.getVelocity().add(vec3d));
    }
}
