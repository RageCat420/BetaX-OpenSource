package dev.niuren.mixins.block;


import cn.trollaura.betax.systems.modules.render.SwordBlock;
import cn.trollaura.betax.util.SwordBlockUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {
/*    @Inject(at = @At("HEAD"), cancellable = true, method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void swordblocking$hideShield(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(!SwordBlock.INSTANCE.isActive()) return;
        if ( (SwordBlockUtil.isWeaponBlocking(entity)))
            leftHanded = true;
            ci.cancel();
    }*/

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.BEFORE, ordinal = 1))
    public void swordblocking$blockingPosition(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(!SwordBlock.INSTANCE.isActive()) return;
        if (!SwordBlockUtil.isWeaponBlocking(player))
            return;
        boolean bl = hand == Hand.MAIN_HAND;
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        int k = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate(k * -SwordBlock.getX().get(), SwordBlock.getY().get(), SwordBlock.getZ().get());
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k * 13.365F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(k * 78.05F));
    }
}
