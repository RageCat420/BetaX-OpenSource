package dev.niuren.mixins;

import cn.trollaura.betax.systems.modules.render.SwordBlock;
import cn.trollaura.betax.util.SwordBlockUtil;
import dev.niuren.BetaX;
import dev.niuren.gui.font.FontAdapter;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.utils.render.Render2DUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tRollaURa_
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    MinecraftClient mc = BetaX.mc;


    @Inject(method = "renderLeftArm",at = @At("HEAD"), cancellable = true)
    public void renderL(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
        if(!SwordBlock.INSTANCE.isActive()) return;
        if(SwordBlockUtil.isWeaponBlocking(player)) {
            ci.cancel();
        }
    }
    /**
     * @author
     * @reason
     */
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",at = @At("HEAD"))
    public void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
/*        if (mc.world != null) {

            EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

            double d = dispatcher.getSquaredDistanceToCamera(entity);
            if (d > 4096.0) {
                continue;
            }

            float f = entity.getNameLabelHeight();
            int i = 0;
            Text text = entity.getName();
            matrices.push();
            matrices.translate(0.0f, f, 0.0f);
            matrices.multiply(dispatcher.getRotation());
            matrices.scale(-0.025f, -0.025f, 0.025f);
            float g = mc.options.getTextBackgroundOpacity(0.25f);
            FontAdapter textRenderer = FontRenderers.Arial;
            int h = (int) (-textRenderer.getWidth(text.getString()) / 2);
            String name = entity.getName().getString();
            String health = " " + entity.getHealth();
*//*

                Text text1 = Text.literal(text.getString()).append(
                    Text.literal(" " + .setStyle(Style.EMPTY.withColor(getColor()))
                );*//*

            Render2DUtils.drawRound(
                matrices,
                (float) h,
                (float) i,
                (float) textRenderer.getWidth(name + health),
                (float) textRenderer.getFontHeight(),
                3.0f,
                new Color(0, 0, 0, 160)
            );



            // 这里可以选择是否绘制文本
                *//*
                textRenderer.draw(
                    text1,
                    h,
                    (float) i,
                    553648127,
                    false,
                    matrices.peek().getModel(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.NORMAL,
                    j,
                    light
                );
                *//*
            matrices.pop();
        }*/

    }



}
