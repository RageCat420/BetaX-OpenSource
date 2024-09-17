package dev.niuren.gui.button;

import cn.trollaura.betax.gui.button.settings.DescriptionSetting;
import cn.trollaura.betax.gui.button.settings.StringSetting;
import dev.niuren.BetaX;
import dev.niuren.gui.Component;
import dev.niuren.gui.button.settings.*;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.ic.Description;
import dev.niuren.ic.Setting;
import dev.niuren.ic.Settings;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import dev.niuren.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.niuren.BetaX.mc;

public class ModuleButton implements Component {
    private final Module module;
    private final List<SettingButton> buttons = new ArrayList<>();

    private int x, y;
    private final int w, h;
    private boolean open;
    private int showingModuleCount;

    private final Color DARK_GRAY = new Color(30, 30, 30, 150);
    private final Color GRAY = new Color(155, 155, 155, 255);
    private final Color BLACK = new Color(20, 20, 20, 255);
    private final Color WHITE = new Color(255, 255, 255, 255);

    public ModuleButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        int n = 0;


        for (Setting<?> setting : Settings.get().getSettingsForMod(module)) {
            SettingButton settingButton = getSettingButton(module, setting, n);

            buttons.add(settingButton);
            n += this.h;
        }
        if(module.getCategory() == Module.Category.HUD) return;

        buttons.add(new DrawnSetting(module, this.x, this.y + this.h + n, this.w, this.h));
        buttons.add(new BindSetting(module, this.x, this.y + this.h + n, this.w, this.h));
    }

    private SettingButton getSettingButton(Module module, Setting<?> setting, int n) {
        return switch (setting.getType()) {
            case Boolean -> new BooleanSetting(module, setting, this.x, this.y + this.h + n, this.w, this.h);
            case Integer -> new IntegerSetting.Slider(module, setting, this.x, this.y + this.h + n, this.w, this.h);
            case Double -> new DoubleSetting.Slider(module, setting, this.x, this.y + this.h + n, this.w, this.h);
            case Page -> null;
            case Mode -> new ModeSetting(module, setting, this.x, this.y + this.h + n, this.w, this.h);
            case Description -> new DescriptionSetting(module, (Setting<Description>) setting,this.x,this.y + this.h + n,this.w,this.h);
            case String -> new StringSetting(module,(Setting<String>)setting,this.x,this.y + this.h + n,this.w,this.h);
        };
    }


    double width = 0;
    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);

        //   Modules.get().get(ClickGUI.class).drawOutline(context, x, y, w, h);

        boolean hover = isHover(x, y, w, h - 1, mouseX, mouseY);
        width = animate(width,module.isActive() ?  x + w - 2 : x + 2);
            BetaX.GUI.drawFlat(context, x + 2, y + 1, (int) width, y + h, hover ? HUD.INSTANCE.colorType().darker().getRGB() : HUD.INSTANCE.color());
            BetaX.GUI.drawFlat(context, x + 2, y + 1, x + w - 2, y + h, hover ? DARK_GRAY.brighter().getRGB() : DARK_GRAY.getRGB());
        if (Modules.get().get(ClickGUI.class).font.get()) {
            FontRenderers.Arial.drawString(context.getMatrices(), module.getName(), x + 5, y + 4, module.isActive() ? WHITE.getRGB() : GRAY.getRGB());
        } else
            context.drawTextWithShadow(mc.textRenderer, Modules.get().get(ClickGUI.class).chinese.get() ? module.getName2() : module.getName(), x + 5, y + 4, module.isActive() ? WHITE.getRGB() : GRAY.getRGB());
        context.drawTextWithShadow(mc.textRenderer, open ? "-" : "+", x + 85, y + 4, module.isActive() ? WHITE.getRGB() : GRAY.getRGB());
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (isHover(x, y, w, h - 1, mouseX, mouseY)) {
            switch (button) {
                case 0 -> {

                    if (!module.getName().equals(Modules.get().get(ClickGUI.class).name)) module.toggle();
                }
                case 1 -> processRightClick();
            }
        }

        if (open) {
            for (SettingButton settingButton : buttons) {
                settingButton.mouseDown(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY) {
        for (SettingButton settingButton : buttons) {
            settingButton.mouseUp(mouseX, mouseY);
        }
    }

    @Override
    public void keyPress(int key) {
        for (SettingButton settingButton : buttons) {
            settingButton.keyPress(key);
        }
    }

    @Override
    public void keyTyped(char charW, int key) {
        for (SettingButton settingButton : buttons) {
            settingButton.keyTyped(charW, key);
        }
    }

    @Override
    public void close() {
        for (SettingButton button : buttons) {
            button.close();
        }
    }

    private boolean isHover(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x * 1.0F && mouseX <= (x + w) * 1.0F && mouseY >= y * 1.0F && mouseY <= (y + h) * 1.0F;
    }

    public void x(int x) {
        this.x = x;
    }

    public void y(int y) {
        this.y = y;
    }

    public boolean open() {
        return this.open;
    }

    public Module module() {
        return module;
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

    public List<SettingButton> buttons() {
        return this.buttons;
    }

    public int moduleCount() {
        return this.showingModuleCount;
    }

    public void processRightClick() {
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f);
        if (!open) {
            showingModuleCount = buttons.size();
            open = true;
        } else {
            showingModuleCount = 0;
            open = false;
        }
    }
}
