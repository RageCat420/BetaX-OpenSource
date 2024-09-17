package dev.niuren.gui;

import dev.niuren.BetaX;
import dev.niuren.gui.button.ModuleButton;
import dev.niuren.gui.button.SettingButton;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.ClickGUI;
import dev.niuren.systems.modules.client.HUD;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

import static dev.niuren.systems.modules.Module.mc;

public class Window implements Component {
    private final ArrayList<ModuleButton> buttons = new ArrayList<>();
    private final Module.Category category;
    private final ArrayList<String> chineseCategory = new ArrayList<>();
    //private final ArrayList<ModuleButton> buttonsBeforeClosing = new ArrayList<>();
    private int x;
    private int y;

    private int catX;
    private int catY;
    private final int w;
    int sbModY;
    private final int h;
    private int dragX;
    private int dragY;
    private boolean open = true;
    private boolean dragging;
    private int totalHeightForBars;
    private int showingButtonCount;

    private final int BLACK = new Color(20, 20, 20, 255).getRGB();

    public Window(Module.Category category, int x, int y, int w, int h) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        int yOffset = this.y + this.h;
        for (Module module : Modules.get().get(category)) {
            ModuleButton button = new ModuleButton(module, this.x, yOffset, this.w + 10, this.h);
            buttons.add(button);
            yOffset += this.h;
            totalHeightForBars++;
        }
        showingButtonCount = buttons.size();
    }
    double speedModule = 0;

    @Override
    public void keyTyped(char charW, int key) {
        for (ModuleButton button : buttons) {
            button.keyTyped(charW,key);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (dragging) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }

        ClickGUI gui = Modules.get().get(ClickGUI.class);
        gui.drawEar(context, true, x - 8, y - 8);
        gui.drawEar(context, false, x + w - 12, y - 8);

        BetaX.GUI.drawFlat(context, x, y, x + w + 10, y + h, HUD.INSTANCE.color());
        ClickGUI clickGUI1 = Modules.get().get(ClickGUI.class);
        if (clickGUI1.icon.get()) {
            switch (category.name()) {
                case "Combat" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawCombat(context, x + 1, y);
                }
                case "Misc" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawMisc(context, x + 1, y);
                }
                case "Movement" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawMovement(context, x + 1, y);
                }
                case "Render" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawRender(context, x + 1, y);
                }
                case "Player" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawPeople(context, x + 1, y);
                }
                case "Client" -> {
                    ClickGUI clickGUI = new ClickGUI();
                    clickGUI.drawClientIcon(context, x + 1, y);
                }
            }
        }
//        gui.drawCatGirl(context, 500, 260);
        context.getMatrices().scale(1.0F, 1.0F, 1.0F);
        float i = clickGUI1.icon.get() ? 20.5f : 0;
        chineseCategory.add("战斗类");
        chineseCategory.add("移动类");
        chineseCategory.add("杂项");
        chineseCategory.add("渲染类");
        chineseCategory.add("玩家类");
        chineseCategory.add("客户端");
        chineseCategory.add("脚本");

        String name;
        if (Modules.get().get(ClickGUI.class).chinese.get()) {
            int index = Math.min(category.ordinal(), chineseCategory.size() - 1);
            name = chineseCategory.get(index);
        } else {
            name = category.name();
        }
        if (Modules.get().get(ClickGUI.class).chinese.get()){
            context.drawTextWithShadow(mc.textRenderer, name, (int) (x + 4 + i), (int) (this.y - 2.0f - (float) -6), -1);
        }
        else  FontRenderers.Arial.drawString(context.getMatrices(), name, x + 4 + i, this.y - 2.0f - (float) -6, -1);
        // context.drawTextWithShadow(mc.textRenderer, category.name(), x + 4, y + 4, -1);
        String moduleCount = "[" + Modules.get().get(category).size() + "]";
        FontRenderers.Arial.drawString(context.getMatrices(), moduleCount, x + 93 - FontRenderers.Arial.getWidth(moduleCount), y + 4, -1);
        // context.drawTextWithShadow(mc.textRenderer, moduleCount, x + 93 - Module.mc.textRenderer.getWidth(moduleCount), y + 4, -1);

        if (open) {
            context.getMatrices().scale(1.0f, 1.0f, 1.0f);
            context.fill(x, y + h, x + w, y + h + 1, BLACK);


            int modY = y + h + 1;
            totalHeightForBars = 0;

            int moduleRenderCount = 0;
            for (ModuleButton moduleButton : buttons) {
                moduleRenderCount++;

                if (moduleRenderCount < showingButtonCount + 1) {
                    moduleButton.x(x);
                    moduleButton.y(modY);

                    moduleButton.render(context, mouseX, mouseY);
                    modY += h;

                    totalHeightForBars++;

                    if (moduleButton.open()) {

                        int settingRenderCount = 0;
                        for (SettingButton settingButton : moduleButton.buttons()) {
                            settingRenderCount++;

                            if (settingRenderCount < moduleButton.moduleCount() + 1) {
                                if (!settingButton.isHidden()) {
                                    settingButton.x(x);

                                    settingButton.y(modY);
                                    settingButton.render(context, mouseX, mouseY);


                                    modY += h;
                                    totalHeightForBars++;
                                }
                            }
                        }
                    }
                }
            }
            context.fill(x, y + h + ((totalHeightForBars) * h) + 2, x + w, y + h + ((totalHeightForBars) * h) + 3, BLACK);
        }
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

    @Override
    public void mouseDown(int mouseX, int mouseY, int button) {
        if (hover(x, y, w, h, mouseX, mouseY)) {
            switch (button) {
                case 0 -> {
                    dragging = true;
                    dragX = x - mouseX;
                    dragY = y - mouseY;
                }
                case 1 -> {
                    if (open) {
                        showingButtonCount = buttons.size();

                        open = false;
                        for (ModuleButton b : buttons) {
                            if (b.open()) {
                                b.processRightClick();
                                //buttonsBeforeClosing.add(b);
                            }
                        }
                    } else {
                        showingButtonCount = buttons.size();

                        open = true;
                        //buttonsBeforeClosing.clear();
                    }
                }
            }
        }

        if (open) {
            for (ModuleButton b : buttons) {
                b.mouseDown(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY) {
        dragging = false;

        if (open) {
            for (ModuleButton button : buttons) {
                button.mouseUp(mouseX, mouseY);
            }
        }
    }

    @Override
    public void keyPress(int key) {
        if (open) {
            for (ModuleButton button : buttons) {
                button.keyPress(key);
            }
        }
    }

    @Override
    public void close() {
        for (ModuleButton button : buttons) {
            button.close();
        }
    }

    private boolean hover(int X, int Y, int W, int H, int mouseX, int mouseY) {
        return mouseX >= X * 1.0F && mouseX <= (X + W) * 1.0F && mouseY >= Y * 1.0F && mouseY <= (Y + H) * 1.0F;
    }

    public Module.Category getCategory() {
        return category;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }

    public int h() {
        return h;
    }
}
