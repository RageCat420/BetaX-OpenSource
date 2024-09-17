package dev.niuren.gui;

import cn.trollaura.betax.gui.drag.Dragger;
import cn.trollaura.betax.gui.drag.Editor;
import cn.trollaura.betax.gui.drag.MaoNiangDragger;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

import static dev.niuren.BetaX.mc;

public class Gui extends Screen {
    @Override
    public void close() {
        (new ClickGUI()).disable();
    }

    public static ArrayList<Window> windows = new ArrayList<>();
    public static ArrayList<Dragger> draggers = new ArrayList<>();

    public Gui() {
        super(Text.literal("ClickGUI"));

        int x = 10;

        for (Module.Category category : Module.Category.values()) {
            if(category.name() != Module.Category.HUD.name()) {
                windows.add(new Window(category, x, 3, 95, 15));
                x += Modules.get().get(ClickGUI.class).ears.get() ? 125 : 110;
            }

        }
        draggers.add(new MaoNiangDragger(500,260,100,100,"Cute Niuru!"));
        draggers.add(new Editor("HUD Editor(Right Click to Move!)"));
    }
    @Override
    public boolean charTyped(char chr, int modifiers) {
        windows.forEach(window -> window.keyTyped(chr, modifiers));
        return false;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);

        context.fill(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        for (Window window : windows) {
            window.render(context, mouseX, mouseY);
        }
        draggers.forEach(dragger -> dragger.render(context,mouseX,mouseY));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        windows.forEach(window -> window.mouseDown((int) mouseX, (int) mouseY, mouseButton));
        draggers.forEach(dragger -> dragger.mouseDown((int) mouseX, (int) mouseY,mouseButton));
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        windows.forEach(window -> window.mouseUp((int) mouseX, (int) mouseY));
        draggers.forEach(dragger -> dragger.mouseUp((int) mouseX, (int) mouseY));
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Window window : windows) {
            window.keyPress(keyCode);
        }

        if (keyCode == 256) {
            mc.setScreen(null);
            Modules.get().get(ClickGUI.class).toggle();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void drawGradient(DrawContext context, int left, int top, int right, int bottom, int startColor, int endColor) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        context.fillGradient(left, top, right, bottom, startColor, endColor);
    }

    public void drawFlat(DrawContext context, int left, int top, int right, int bottom, int startColor) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        context.fillGradient(left, top, right, bottom, startColor, startColor);
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        ClickGUI gui = Modules.get().get(ClickGUI.class);

        if (verticalAmount < 0) {
            for (Window window : windows) {
                window.y(window.y() - gui.scrollFactor.get());
            }
        } else if (verticalAmount > 0) {
            for (Window window : windows) {
                window.y(window.y() + gui.scrollFactor.get());
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
