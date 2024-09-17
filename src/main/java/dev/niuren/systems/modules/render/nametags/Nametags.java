package dev.niuren.systems.modules.render.nametags;

import dev.niuren.events.event.RenderEvent;
import dev.niuren.gui.font.FontAdapter;
import dev.niuren.gui.font.FontRenderer;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.render.Render2DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tRollaURa_
 * @since 2024/9/15
 *
 */
@Module.Info(name = "Nametags",chineseName = "名称标签",category = Module.Category.Render, description = "")
public class Nametags extends Module {
    public static Nametags INSTANCE;
    public Nametags() {
        super("Nametags","名称标签",Category.Render,"");
        INSTANCE = this;
    }

/*    @EventHandler
    public void onRender(RenderEvent event) {
        MatrixStack matrices = event.getDrawContext().getMatrices();
        if (mc.world != null) {
            for (PlayerEntity entity : mc.world.getPlayers()) {

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

                FontRenderers.Arial.drawString(matrices,text.getString(),h,i,-1);
                FontRenderers.Arial.drawString(matrices, String.valueOf(entity.getHealth()),textRenderer.getWidth(name+ health) - textRenderer.getWidth(health) -1 ,i,getColor(entity.getHealth()));

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
            }
        }
    }*/

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
