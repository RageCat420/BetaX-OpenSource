package dev.niuren.systems.modules.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.niuren.BetaX;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;

@Module.Info(name = "ClickGUI", category = Module.Category.Client, bind = 345, drawn = false, description = "", chineseName = "界面")
public class ClickGUI extends Module {
    public Setting<Integer> r = register("Red", 144, 0, 255);
    public Setting<Integer> g = register("Green", 144, 0, 255);
    public Setting<Integer> b = register("Blue", 144, 0, 255);
    public Setting<Boolean> chinese = register("Chinese", true);
    public Setting<Boolean> ears = register("Ears", false);
    public Setting<Boolean> icon = register("Icon", false);
    public Setting<Boolean> checkbox = register("Checkbox", true);
    public Setting<Boolean> font = register("CustomFont", true);
    public Setting<Integer> sliderHeight = register("SHeight", 14, 1, 14);
    public Setting<Boolean> sliderBackground = register("SBackground", true);
    public Setting<Integer> scrollFactor = register("ScrollFactor", 5, 0, 30);
    public Setting<String> outline = register("Outline", List.of("Normal", "Rounded", "OFF"), "Rounded");
    public Setting<Integer> alpha = register("Alpha", 144, 0, 255);

    @Override
    public void onActivate() {
        mc.setScreen(BetaX.GUI);
    }

    @Override
    public void onDeactivate() {
        mc.setScreen(null);
    }

    public void drawCheckBox(DrawContext context, boolean checked, int x, int y) {
        if (!checkbox.get()) return;

        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", checked ? "clickgui/checked.png" : "clickgui/unchecked.png"), x, y, 0, 0, 10, 10, 10, 10);
        RenderSystem.disableBlend();
    }

    public void drawCatGirl(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/deobf.png"), x, y, 0, 0, 100, 100, 100, 100);
        RenderSystem.disableBlend();
    }

    public void drawEar(DrawContext context, boolean left, int x, int y) {
        if (!ears.get()) return;
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", left ? "clickgui/left_ear.png" : "clickgui/right_ear.png"), x, y, 0, 0, 20, 20, 20, 20);
        RenderSystem.disableBlend();
    }

    public void drawCombat(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/sword.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawMisc(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/misc.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawPeople(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/people.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawRender(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/render.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawMovement(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/movement.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawClientIcon(DrawContext context, int x, int y) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        context.drawTexture(new Identifier("beta-x", "clickgui/gear.png"), x, y, 0, 0, 20, 15, 20, 15);
        RenderSystem.disableBlend();
    }

    public void drawOutline(DrawContext context, int x, int y, int w, int h) {
        int color = new Color(r.get(), g.get(), b.get(), alpha.get()).getRGB();

        switch (outline.get()) {
            case "Normal" -> normalOutline(context, x, y, w, h, color);
            case "Rounded" -> roundedOutline(context, x, y, w, h, color);
        }
    }

    private void roundedOutline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x + 1, y + 1, x + 2, y + h, color);
        drawLine(context, x, y, w, h, color);
        context.fill(x + w - 1, y + 1, x + w - 2, y + h, color);
        context.fill(x + 2, y + h, x + w - 2, y + h + 1, color);
    }

    private void normalOutline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x + 1, y + 1, x + 2, y + h, color);
        drawLine(context, x, y, w, h, color);
        context.fill(x + w - 1, y + 1, x + w - 2, y + h + 1, color);
        context.fill(x + 1, y + h, x + w - 2, y + h + 1, color);
    }

    public void drawLine(DrawContext context, int x, int y, int w, int h, int color) {
        switch (outline.get()) {
            case "Normal" -> context.fill(x + 1, y + 1, x + w - 1, y, color);
            case "Rounded" -> context.fill(x + 2, y + 1, x + w - 2, y, color);
        }
    }
}
