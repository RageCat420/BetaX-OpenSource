package dev.niuren.gui.button.settings;

import dev.niuren.BetaX;
import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import dev.niuren.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import static dev.niuren.BetaX.mc;

public class DoubleSetting extends SettingButton {
    private final Setting<Double> setting;
    protected boolean dragging;
    protected double sliderWidth;

    public DoubleSetting(Module module, Setting setting, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.dragging = false;
        this.sliderWidth = 0;
        this.setting = setting;
    }

    protected void updateSlider(int mouseX) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        this.setHidden(!setting.isVisible());
        updateSlider(mouseX);

        drawButton(context);
        drawDescription(context, mouseX, mouseY, this.setting.getDescription(), this);

        MatrixStack stack = new MatrixStack();
        stack.scale(1.0F, 1.0F, 1.0F);

        // Draws background
        boolean hover = hover(x(), y(), w(), h() - 1, mouseX, mouseY);
        drawBackground(context, Modules.get().get(ClickGUI.class).sliderBackground.get());
        BetaX.GUI.drawFlat(context, x() + 3, y() + Modules.get().get(ClickGUI.class).sliderHeight.get(), (int) (x() - 2 + (sliderWidth) + 5), y() + h(), hover ? HUD.INSTANCE.colorType().darker().getRGB() : HUD.INSTANCE.color());

        FontRenderers.Arial.drawString(context.getMatrices(), setting.getName(), (x() + 6), y() + 4, WHITE);
        // context.drawTextWithShadow(mc.textRenderer, setting.getName(), x() + 6, y() + 4, WHITE);
        FontRenderers.Arial.drawString(context.getMatrices(), String.format("%." + setting.getInc() + "f", setting.get()).replace(",", "."), (x() + 6) + mc.textRenderer.getWidth(" " + setting.getName()), y() + 4, GRAY);
        // context.drawTextWithShadow(mc.textRenderer, String.format("%." + setting.getInc() + "f", setting.get()).replace(",", "."), (x() + 6) + mc.textRenderer.getWidth(" " + setting.getName()), y() + 4, GRAY);
    }

    private void drawBackground(DrawContext context, boolean background) {
        if (background) {
            BetaX.GUI.drawFlat(context, x() + 3, y() + 1, x() + w() - 3, y() + h(), BLACK);
        } else {
            BetaX.GUI.drawFlat(context, (int) (x() + 3 + (sliderWidth)), y() + Modules.get().get(ClickGUI.class).sliderHeight.get(), x() + w() - 3, y() + h(), BLACK);
        }
    }

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x(), y(), w(), h() - 1, mouseX, mouseY) && button == 0) {
            if (!dragging) mc.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f);
            dragging = true;
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY) {
        dragging = false;
    }

    @Override
    public void close() {
        dragging = false;
    }

    public static class Slider extends DoubleSetting {
        private final Setting<Double> setting;

        public Slider(Module module, Setting setting, int x, int y, int w, int h) {
            super(module, setting, x, y, w, h);
            this.setting = setting;
        }

        @Override
        protected void updateSlider(int mouseX) {
            final double diff = Math.min(w(), Math.max(0, mouseX - x()));
            final double min = setting.getMin();
            final double max = setting.getMax();
            sliderWidth = (w() - 6) * (setting.get() - min) / (max - min);

            if (dragging) {
                setting.setValue(diff == 0.0 ? setting.getMin() : Double.parseDouble(String.format("%." + setting.getInc() + "f", diff / w() * (max - min) + min).replace(",", ".")));
            }
        }
    }
}
