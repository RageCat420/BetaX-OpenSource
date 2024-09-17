package dev.niuren.mixins;

import dev.niuren.gui.font.FontAdapter;
import dev.niuren.gui.font.FontRenderer;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.render.nametags.Nametags;
import dev.niuren.utils.render.Render2DUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity>{
    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void onRenderLabel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (entity instanceof PlayerEntity) {
            if ((Nametags.INSTANCE.isActive())) {
                DrawContext drawContext = new DrawContext(mc,mc.getBufferBuilders().getEntityVertexConsumers());

                    double d = dispatcher.getSquaredDistanceToCamera(entity);
                    if (!(d > 4096.0)) {
                        float f = entity.getNameLabelHeight();
                        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
                        matrices.push();
                        matrices.translate(0.0F, f, 0.0F);
                        matrices.multiply(dispatcher.getRotation());
                        matrices.scale(-0.025F, -0.025F, 0.025F);
                        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                        float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                        int j = (int) (g * 255.0F) << 24;
                        FontAdapter textRenderer = FontRenderers.Arial;
                        int h = (int) (-textRenderer.getWidth(text.getString()) / 2);
                        String name = entity.getName().getString();
                        String health = " " + ((PlayerEntity) entity).getHealth() + ((PlayerEntity) entity).getAbsorptionAmount();
/*
                        Text text1 = Text.literal(text.getString()).append(
                            Text.literal(" " + .setStyle(Style.EMPTY.withColor(getColor()))
                        );*/

                        Render2DUtils.drawRound(
                            matrices,
                            (float) h,
                            (float) i,
                            (float) textRenderer.getWidth(name + health) + 6,
                            (float) textRenderer.getFontHeight(),
                            1.0f,
                            new Color(0, 0, 0, 160)
                        );
                        Render2DUtils.drawRound(
                            matrices,
                            (float) h,
                            ((float) i + (float) textRenderer.getFontHeight()),
                            ((float) textRenderer.getWidth(name + health) + 6) * ((((PlayerEntity) entity).getHealth() + ((PlayerEntity) entity).getAbsorptionAmount()) / (((PlayerEntity) entity).getMaxHealth() + ((PlayerEntity) entity).getMaxAbsorption())),
                            1,
                            1.0f,
                            new Color(0,255,0,255)
                        );
                        FontRenderers.Arial.drawString(matrices,text.getString(),h,i + 2,-1);
                        FontRenderers.Arial.drawString(matrices, health,textRenderer.getWidth(name+ health) - textRenderer.getWidth(health) -1 - 18 ,i + 2,getColor(((PlayerEntity) entity).getHealth()));
                        drawContext.drawItem(((PlayerEntity) entity).getEquippedStack(EquipmentSlot.HEAD), h, i + 1);
                        matrices.pop();
                    }
                info.cancel();
            }
        }

    }

    @Unique
    public int getColor(float health) {
        if (health >= 20f) {
            return Color.GREEN.getRGB();
        } else if (health >= 10f) {
            return Color.YELLOW.getRGB();
        } else if (health >= 0f) {
            return Color.RED.getRGB();
        }
        return Color.RED.getRGB();
    }
}
