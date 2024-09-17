package dev.niuren.mixins.block;

import cn.trollaura.betax.systems.modules.render.SwordBlock;
import cn.trollaura.betax.util.SwordBlockUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends LivingEntity> {
    @Shadow protected abstract void positionRightArm(T entity);

    @Shadow protected abstract void positionLeftArm(T entity);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V", shift = At.Shift.BEFORE), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void swordblockingSetBlockingAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if(!SwordBlock.INSTANCE.isActive()) return;
        if (!SwordBlockUtil.isWeaponBlocking(livingEntity))
            return;
        this.positionRightArm(livingEntity);
    }
}
