package dev.niuren.gui.button.settings;

import dev.niuren.BetaX;
import dev.niuren.events.event.StateEvent;
import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class BooleanSetting extends SettingButton {
    private final Setting<Boolean> setting;

    public BooleanSetting(Module module, Setting setting, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.setting = setting;
    }

    double width_ = 0;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        this.setHidden(!setting.isVisible());

        drawButton(context);
        drawDescription(context, mouseX, mouseY, this.setting.getDescription(), this);

        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        width_ = animate(width_,setting.get() ? x() + w() - 3 : x() + 3);
        BetaX.GUI.drawFlat(context, x() + 3, y() + 1, (int) width_, y() + h(), setting.get() ? new Color(Modules.get().get(ClickGUI.class).r.get(), Modules.get().get(ClickGUI.class).g.get(), Modules.get().get(ClickGUI.class).b.get(), Modules.get().get(ClickGUI.class).alpha.get()).getRGB() : BLACK);
        Modules.get().get(ClickGUI.class).drawCheckBox(context, setting.get(), x() + w() - 13, y() + 3);
        FontRenderers.Arial.drawString(context.getMatrices(), setting.getName(), x() + 6, y() + 4, setting.get() ? WHITE : GRAY);
        //  context.drawTextWithShadow(mc.textRenderer, setting.getName(), x() + 6, y() + 4, setting.get() ? WHITE : GRAY);
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x(), y(), w(), h() - 1, mouseX, mouseY) && button == 0) {
;

            setting.setValue(!setting.get());
            BetaX.EVENT_BUS.post(new StateEvent.Button(this.module(), setting));
        }
    }
}
