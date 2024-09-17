package dev.niuren.gui.button;

import dev.niuren.BetaX;
import dev.niuren.gui.Component;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import dev.niuren.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.niuren.BetaX.mc;

/**
 * @author tRollaURa_
 */
public class SettingButton implements Component {
    private Module module;
    boolean hidden = false;

    private int x, y, w, h;

    protected final int WHITE = new Color(255, 255, 255, 255).getRGB();
    protected final int GRAY = new Color(155, 155, 155, 255).getRGB();
    protected final int BLACK = new Color(20, 20, 20, 255).getRGB();

    public SettingButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
    }


    @Override
    public void mouseUp(int mouseX, int mouseY) {
    }

    @Override
    public void keyPress(int key) {
    }

    @Override
    public void keyTyped(char charW, int key) {

    }

    @Override
    public void close() {
    }


    public void drawButton(DrawContext context) {
        ClickGUI gui = Modules.get().get(ClickGUI.class);
        int HUD = new Color(gui.r.get(), gui.g.get(), gui.b.get(), gui.alpha.get()).getRGB();
        context.getMatrices().scale(1.0f, 1.0f, 1.0f);

        // Background
        context.fill(x() + 3, y(), x() + w() - 3, y() + h(), BLACK);
        // Sides
        context.fill(x() + 3, y(), x() + 2, y() + h(), HUD);
        context.fill(x() + w() - 3, y(), x() + w() - 2, y() + h(), HUD);
    }

    public void drawButton(DrawContext context, int mouseX, int mouseY) {
        boolean hover = hover(x(), y(), w(), h() - 1, mouseX, mouseY);
        BetaX.GUI.drawFlat(context, x() + 3, y() + 1, x() + w() - 3, y() + h(), hover ? HUD.INSTANCE.colorType().darker().getRGB() : HUD.INSTANCE.color());
    }

    public void drawDescription(DrawContext context, int mouseX, int mouseY, String settingDescription, SettingButton clazz) {
        if (!hover(x(), y(), w(), h() - 1, mouseX, mouseY)) return;

        String description = settingDescription == null ? "A Setting. (" + clazz.getClass().getSimpleName().replace("Setting", "") + ")" : settingDescription;

        int x = mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(description);
        int y = mc.getWindow().getScaledHeight() - 10;

        context.fill(x - 2, y - 2, (int) (x + mc.textRenderer.getWidth(description) + 1 * 1.5D), y + mc.textRenderer.fontHeight + 2, new Color(50, 50, 50).getRGB());
        FontRenderers.Arial.drawString(context.getMatrices(), description, x, y, WHITE);
        // context.drawTextWithShadow(mc.textRenderer, description, x, y, -1);
    }

    public int height() {
        return h;
    }

    public Module module() {
        return this.module;
    }

    public void module(Module module) {
        this.module = module;
    }

    public boolean drawn() {
        return this.module().drawn;
    }

    public void drawn(boolean drawn) {
        this.module().drawn = drawn;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }

    public int w() {
        return w;
    }

    public void w(int w) {
        this.w = w;
    }

    public int h() {
        return h;
    }

    public void h(int h) {
        this.h = h;
    }
    public static double animate(double current, double endPoint) {
        return animate(current, endPoint, (double) cn.trollaura.betax.systems.modules.client.Component.INSTANCE.getAnimationSpeed().get() / 10);
    }

    public static double animate(double current, double endPoint, double speed) {
        boolean shouldContinueAnimation = endPoint > current;

        double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        double factor = dif * speed;
        if (Math.abs(factor) <= 0.001) return endPoint;
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    public boolean hover(int X, int Y, int W, int H, int mouseX, int mouseY) {
        return mouseX >= X * 1.0F && mouseX <= (X + W) * 1.0F && mouseY >= Y * 1.0F && mouseY <= (Y + H) * 1.0F;
    }
}
