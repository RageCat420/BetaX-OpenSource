package dev.niuren.systems.modules.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.niuren.events.event.EntityDestroyEvent;
import dev.niuren.events.event.RenderEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;

/**
 * @author NiuRen0827
 * Time:11:19
 */
@Module.Info(name = "CombatHud", category = Module.Category.Client, description = "", chineseName = "战斗界面")
public class CombatHud extends Module {
    boolean shouldRender = false;

    @EventHandler
    public void invoke(EntityDestroyEvent event) {
        if (event.entity instanceof EndCrystalEntity) {
            shouldRender = true;
        }
    }

    @EventHandler
    public void onRender(RenderEvent event) {
        int width = event.getDrawContext().getScaledWindowWidth();
        int height = event.getDrawContext().getScaledWindowHeight();

        int centerX = width / 2;
        int centerY = height / 2;
        if (shouldRender) {
            drawHitMark(event.getDrawContext(), centerX - 8, centerY - 7);
            shouldRender = false;
        }

    }

    public void drawHitMark(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "textures/hitmarker.png"), x, y, 0, 0, 15, 15, 15, 15);
        RenderSystem.disableBlend();
    }
}
