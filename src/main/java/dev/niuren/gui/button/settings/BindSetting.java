package dev.niuren.gui.button.settings;

import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import dev.niuren.utils.key.KeyName;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

import static dev.niuren.BetaX.mc;

public class BindSetting extends SettingButton {
    private final Module module;
    private boolean binding;

    public BindSetting(Module module, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.module = module;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        drawButton(context);
        drawButton(context, mouseX, mouseY);

        context.getMatrices().scale(1.0F, 1.0F, 1.0F);

        ClickGUI gui = Modules.get().get(ClickGUI.class);
        int color = new Color(gui.r.get(), gui.g.get(), gui.b.get(), gui.alpha.get()).getRGB();
        gui.drawLine(context, x(), y(), w(), h(), color);
        FontRenderers.Arial.drawString(context.getMatrices(), "Bind", x() + 6, y() + 4, WHITE);
        // context.drawTextWithShadow(mc.textRenderer, "Bind", x() + 6, y() + 4, WHITE);
        FontRenderers.Arial.drawString(context.getMatrices(), binding ? "..." : KeyName.get(module.getBind()), (x() + 6) + FontRenderers.Arial.getWidth(" Bind"), y() + 4, GRAY);
        // context.drawTextWithShadow(mc.textRenderer, binding ? "..." : KeyName.get(module.getBind()), (x() + 6) + mc.textRenderer.getWidth(" Bind"), y() + 4, GRAY);
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x(), y(), w(), h() - 1, mouseX, mouseY)) {
;
            binding = !binding;
        }
    }

    @Override
    public void keyPress(int key) {
        if (binding) {
            if (key == 256 || key == 259 || key == 261) {
                this.module().setBind(-1);
            } else module().setBind(key);

            binding = false;
        }
    }
}
