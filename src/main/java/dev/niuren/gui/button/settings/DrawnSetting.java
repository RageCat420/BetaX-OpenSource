package dev.niuren.gui.button.settings;

import dev.niuren.BetaX;
import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

import static dev.niuren.BetaX.mc;

public class DrawnSetting extends SettingButton {
    public DrawnSetting(Module module, int x, int y, int w, int h) {
        super(module, x, y, w, h);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        drawButton(context);

        MatrixStack stack = new MatrixStack();
        stack.scale(1.0F, 1.0F, 1.0F);

        if (this.drawn()) {
            drawButton(context, mouseX, mouseY);
        } else {
            boolean hover = hover(x(), y(), w(), h() - 1, mouseX, mouseY);
            BetaX.GUI.drawFlat(context, x() + 3, y() + 1, x() + w() - 3, y() + h(), hover ? new Color(BLACK).brighter().getRGB() : BLACK);
        }

        FontRenderers.Arial.drawString(context.getMatrices(), "Drawn", x() + 6, y() + 4, this.module().drawn ? WHITE : GRAY);
        // context.drawTextWithShadow(mc.textRenderer, "Drawn", x() + 6, y() + 4, this.module().drawn ? WHITE : GRAY);
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x(), y(), w(), h() - 1, mouseX, mouseY) && (button == 0 || button == 1)) {
;
            this.drawn(!this.drawn());
        }
    }
}
