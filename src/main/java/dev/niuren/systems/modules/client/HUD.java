package dev.niuren.systems.modules.client;

import cn.trollaura.betax.gui.HUDGui;
import dev.niuren.events.event.GameJoinedEvent;
import dev.niuren.events.event.PacketEvent;
import dev.niuren.events.event.RenderEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.utils.Utils;
import dev.niuren.utils.render.ColorUtil;
import dev.niuren.utils.render.Render2DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@Module.Info(name = "Hud", category = Module.Category.Client, description = "", chineseName = "HUD")
public class HUD extends Module {
    public static HUD INSTANCE = new HUD();
    public Setting<Boolean> brighter = register("Brighter", false);
    public Setting<Boolean> arrayList = register("ArrayList", true);
    public Setting<String> arraySort = register("Sort", Arrays.asList("Length", "ABC"), "Length");
    public Setting<Boolean> TPS = register("TPS", false);
    public Setting<String> rendering = register("Rendering", Arrays.asList("Up", "Down"), "Up");

    private final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1;
    private long timeGameJoined;

    public HUD() {
        INSTANCE = this;
    }

    private static double animation(double current, double end, double factor, double start) {
        double progress = (end - current) * factor;

        if (progress > 0) {
            progress = Math.max(start, progress);
            progress = Math.min(end - current, progress);
        } else if (progress < 0) {
            progress = Math.min(-start, progress);
            progress = Math.max(end - current, progress);
        }

        return current + progress;
    }

    @EventHandler
    public void onRender(RenderEvent event) {
        HUDGui.INSTANCE.getDraggers().forEach(it -> {
            if(it.isActive()) {
                it.renderDragger(event.getDrawContext());
            }
        });
        if (Utils.cantUpdate()) return;

        int arrayModules = 0;
        int hudElements = 0;




        if (arrayList.get()) {
            ArrayList<Module> modules = new ArrayList<>(Modules.get().getModules());
            switch (arraySort.get()) {
                case ("Length"):
                    modules.sort(Comparator.comparing(m -> -(mc.textRenderer.getWidth(m.getName()) + mc.textRenderer.getWidth(m.getDisplayInfo()))));
                case ("ABC"):
                    modules.sort(Comparator.comparing(m -> getName() + getDisplayInfo()));
            }
            for (Module value : modules) {
                Module module;
                module = value;
                if(module.getCategory() != Category.HUD) {
                    if (module.isDrawn() && module.isActive()) {
                        int x = mc.getWindow().getScaledWidth();
                        int y = rendering.get().equalsIgnoreCase("Up") ? 3 + (arrayModules * 10) : mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight - 1 - (arrayModules * 10);
                        int lWidth = mc.textRenderer.getWidth(module.getName() + module.getDisplayInfo());

                        if (mc.currentScreen instanceof ChatScreen && rendering.get().equalsIgnoreCase("Down"))
                            y = y - 15;

                        if (module.arrayAnimation < lWidth && module.isActive()) {
                            module.arrayAnimation = animation(module.arrayAnimation, lWidth + 5, 1, 0.1);
                        } else if (module.arrayAnimation > 1.5f && !module.isActive()) {
                            module.arrayAnimation = animation(module.arrayAnimation, -1.5, 1, 0.1);
                        } else if (module.arrayAnimation <= 1.5f && !module.isActive()) {
                            module.arrayAnimation = -1f;
                        }
                        if (module.arrayAnimation > lWidth && module.isActive()) {
                            module.arrayAnimation = lWidth;
                        }

                        x -= module.arrayAnimation;
                        Render2DUtils.drawRound(event.getDrawContext().getMatrices(), x - 5, y - 2, event.getDrawContext().getScaledWindowWidth(), mc.textRenderer.fontHeight + 2, 0f, new Color(0, 0, 0, 120));
                        event.getDrawContext().fill(event.getDrawContext().getScaledWindowWidth() - 2, y, event.getDrawContext().getScaledWindowWidth(), y + mc.textRenderer.fontHeight, new Color(255, 255, 255, 255).getRGB());
                        event.getDrawContext().drawTextWithShadow(mc.textRenderer, module.getName() + module.getDisplayInfo(), x - 2, y, ColorUtil.interpolateColorsBackAndForth(3,90, new Color(100,100,200,255),new Color(223,124,125,255),true).getRGB());

                        arrayModules++;
                    }
                }
            }
        }





        if (TPS.get()) {
            int x = mc.getWindow().getScaledWidth();
            int y = rendering.get().equalsIgnoreCase("Up") ? mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight - 1 - (hudElements * 10) : 3 + (hudElements * 10);
            if (mc.currentScreen instanceof ChatScreen && rendering.get().equalsIgnoreCase("Up")) y = y - 15;

            String content = Formatting.GRAY + "TPS " + Formatting.WHITE + String.format("%.1f", getTickRate());

            int x2 = x - mc.textRenderer.getWidth(content) - 2;
            event.getDrawContext().drawTextWithShadow(mc.textRenderer, content, x2, y, -1);

            hudElements++;
        }




    }

    //tickrate

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if (event.packet instanceof WorldTimeUpdateS2CPacket) {
            long now = System.currentTimeMillis();
            float timeElapsed = (float) (now - timeLastTimeUpdate) / 1000.0F;
            tickRates[nextIndex] = clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            nextIndex = (nextIndex + 1) % tickRates.length;
            timeLastTimeUpdate = now;
        }
    }

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        Arrays.fill(tickRates, 0);
        nextIndex = 0;
        timeGameJoined = timeLastTimeUpdate = System.currentTimeMillis();
    }

    public float getTickRate() {
        if (mc.world == null || mc.player == null) return 0;
        if (System.currentTimeMillis() - timeGameJoined < 4000) return 20;

        int numTicks = 0;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (tickRate > 0) {
                sumTickRates += tickRate;
                numTicks++;
            }
        }
        return sumTickRates / numTicks;
    }

    public float getTimeSinceLastTick() {
        long now = System.currentTimeMillis();
        if (now - timeGameJoined < 4000) return 0;
        return (now - timeLastTimeUpdate) / 1000f;
    }


    private float clamp(float value, float min, float max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    private String getString(double pos) {
        return String.format("%.1f", pos).replace(',', '.');
    }



    private double worldCords(double cords) {
        if (mc.world.getDimension().respawnAnchorWorks()) return (cords * 8);
        return cords / 8;
    }



    private double getSpeed() {
        if (mc.player == null) return 0;

        double tX = Math.abs(mc.player.getX() - mc.player.prevX);
        double tZ = Math.abs(mc.player.getZ() - mc.player.prevZ);
        double length = Math.sqrt(tX * tX + tZ * tZ);

//        TimerRewrite timer = Hack.modules().get().get(TimerRewrite.class);
//        if (timer.isActive()) length *= Hack.modules().get().get(TimerRewrite.class).getMultiplier();

        return (length * 20) * 3.6;
    }

    public Color color(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public int color() {
        ClickGUI gui = Modules.get().get(ClickGUI.class);
        return brighter.get() ? new Color(gui.r.get(), gui.g.get(), gui.b.get()).brighter().getRGB() : new Color(gui.r.get(), gui.g.get(), gui.b.get()).getRGB();
    }

    public Color colorType() {
        ClickGUI gui = Modules.get().get(ClickGUI.class);
        return brighter.get() ? new Color(gui.r.get(), gui.g.get(), gui.b.get()).brighter() : new Color(gui.r.get(), gui.g.get(), gui.b.get());
    }

    public Color colorType(int alpha) {
        ClickGUI gui = Modules.get().get(ClickGUI.class);
        return brighter.get() ? new Color(gui.r.get(), gui.g.get(), gui.b.get(), alpha).brighter() : new Color(gui.r.get(), gui.g.get(), gui.b.get(), alpha);
    }

    public int revertColor() {
        ClickGUI gui = Modules.get().get(ClickGUI.class);
        return !brighter.get() ? new Color(gui.r.get(), gui.g.get(), gui.b.get()).brighter().getRGB() : new Color(gui.r.get(), gui.g.get(), gui.b.get()).getRGB();
    }
}
