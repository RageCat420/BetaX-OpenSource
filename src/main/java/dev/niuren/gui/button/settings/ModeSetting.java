package dev.niuren.gui.button.settings;

import dev.niuren.BetaX;
import dev.niuren.events.event.StateEvent;
import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import static dev.niuren.BetaX.mc;

public class ModeSetting extends SettingButton {
    private final Setting<String> setting;

    public ModeSetting(Module module, Setting setting, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.setting = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        this.setHidden(!setting.isVisible());
        drawButton(context);
        drawDescription(context, mouseX, mouseY, this.setting.getDescription(), this);

        MatrixStack stack = new MatrixStack();
        stack.scale(1.0F, 1.0F, 1.0F);

        drawButton(context, mouseX, mouseY);

        FontRenderers.Arial.drawString(context.getMatrices(), setting.getName(), x() + 6, y() + 4, WHITE);
        // context.drawTextWithShadow(mc.textRenderer, setting.getName(), x() + 6, y() + 4, WHITE);
        FontRenderers.Arial.drawString(context.getMatrices(), setting.get(), (x() + 6) + mc.textRenderer.getWidth(" " + setting.getName()), y() + 4, GRAY);
        // context.drawTextWithShadow(mc.textRenderer, setting.get(), (x() + 6) + mc.textRenderer.getWidth(" " + setting.getName()), y() + 4, GRAY);
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x(), y(), w(), h() - 1, mouseX, mouseY)) {
;
            if (button == 0) {
                int i = 0;
                int enumIndex = 0;
                for (String enumName : setting.getModes()) {
                    if (enumName.equals(setting.get()))
                        enumIndex = i;
                    i++;
                }
                if (enumIndex == setting.getModes().size() - 1) {
                    String mode = setting.getModes().get(0);
                    setting.setValue(mode);

                    BetaX.EVENT_BUS.post(new StateEvent.Mode(this.module(), mode));
                } else {
                    enumIndex++;
                    i = 0;
                    for (String enumName : setting.getModes()) {
                        if (i == enumIndex) {
                            setting.setValue(enumName);
                            BetaX.EVENT_BUS.post(new StateEvent.Mode(this.module(), enumName));
                        }
                        i++;
                    }
                }
            } else if (button == 1) {
                int i = 0;
                int enumIndex = 0;
                for (String enumName : setting.getModes()) {
                    if (enumName.equals(setting.get()))
                        enumIndex = i;
                    i++;
                }
                if (enumIndex == 0) {
                    String mode = setting.getModes().get(setting.getModes().size() - 1);
                    setting.setValue(mode);

                    BetaX.EVENT_BUS.post(new StateEvent.Mode(this.module(), mode));
                } else {
                    enumIndex--;
                    i = 0;
                    for (String enumName : setting.getModes()) {
                        if (i == enumIndex) {
                            setting.setValue(enumName);
                            BetaX.EVENT_BUS.post(new StateEvent.Mode(this.module(), enumName));
                        }
                        i++;
                    }
                }
            }
        }
    }
}
