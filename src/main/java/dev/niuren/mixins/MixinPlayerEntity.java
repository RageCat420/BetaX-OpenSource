package dev.niuren.mixins;

import cn.trollaura.betax.systems.modules.combat.NoClickDelay;
import dev.niuren.BetaX;
import dev.niuren.events.event.JumpEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.niuren.systems.modules.Module.mc;

/**
 * @author NiuRen0827
 * Time:13:30
 */
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {


    @Shadow
    public abstract boolean isMainPlayer();

    @Shadow
    public int experienceLevel;

    public MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author niuren
     * @reason bypass
     */
    @Overwrite
    public void jump() {
        float yaw = this.getYaw();
        Vec3d vec3d = this.getVelocity();
        if (this.getId() == mc.player.getId()) {
            JumpEvent event = new JumpEvent(this.getYaw());
            BetaX.EVENT_BUS.post(event);
            yaw = (float) event.getMotionY();
            this.setVelocity(vec3d.x, (double) this.getJumpVelocity(), vec3d.z);
            if (this.isSprinting()) {
                float f = (float) (yaw * 0.017453292F);
                this.setVelocity(this.getVelocity().add((double) (-MathHelper.sin(f) * 0.2F), 0.0, (double) (MathHelper.cos(f) * 0.2F)));
            }
        } else {
            this.setVelocity(vec3d.x, (double) this.getJumpVelocity(), vec3d.z);
            if (this.isSprinting()) {
                float f = (float) (this.getYaw() * 0.017453292F);
                this.setVelocity(this.getVelocity().add((double) (-MathHelper.sin(f) * 0.2F), 0.0, (double) (MathHelper.cos(f) * 0.2F)));
            }
        }

        this.velocityDirty = true;

    }

    @Inject(method = "getAttackCooldownProgressPerTick", at = @At("HEAD"), cancellable = true)
    private void modifyAttackCooldown(CallbackInfoReturnable<Float> cir) {
        if(!NoClickDelay.INSTANCE.isActive()) return;
        cir.setReturnValue(0.0F);
    }


    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }
}
