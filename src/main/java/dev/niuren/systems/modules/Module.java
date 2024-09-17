package dev.niuren.systems.modules;

import dev.niuren.BetaX;
import dev.niuren.ic.Setting;
import dev.niuren.ic.Settings;
import dev.niuren.utils.Utils;
import dev.niuren.utils.player.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public class Module extends ChatUtils {
    public static MinecraftClient mc = MinecraftClient.getInstance();

    public String name;
    public String name2;
    public String displayInfo;
    public int bind;
    public boolean toggled;
    public boolean drawn;
    public double arrayAnimation = -1;
    public Category category;
    public String description;

    private boolean silent = false;

    public Module() {
        Info info = getClass().getAnnotation(Info.class);
        this.bind = info.bind();
        this.name = info.name();
        this.name2 = info.chineseName();
        this.displayInfo = "";
        this.category = info.category();
        this.toggled = false;
        this.description = info.description();
        this.drawn = info.drawn();
    }

    public Module(String name,String chineseName, Category category, String description) {
        this.bind = -1;
        this.name = name;
        this.name2 = chineseName;
        this.displayInfo = "";
        this.category = category;
        this.toggled = false;
        this.description = description;
        this.drawn = true;
    }

    public void onActivate() {
    }

    public void onDeactivate() {
    }

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(String info) {
        info = info.replace(",", Formatting.GRAY + "," + Formatting.WHITE);
        this.displayInfo = Formatting.GRAY + " [" + Formatting.WHITE + info + Formatting.GRAY + "]";
    }

    public Category getCategory() {
        return category;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int key) {
        this.bind = key;
    }

    public boolean isBounded() {
        return this.bind != GLFW.GLFW_KEY_UNKNOWN;
    }

    public boolean isActive() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (toggled) enable();
        else disable();
    }

    public void toggle() {
        setToggled(!this.toggled);
    }

    public void toggle(boolean silent) {
        this.silent = silent;

        this.toggled = !this.toggled;
        if (toggled) enable();
        else disable();
    }

    public boolean isDrawn() {
        return drawn;
    }

    public boolean nullCheck() {
        return mc == null || mc.player == null || mc.world == null;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void enable() {
        BetaX.EVENT_BUS.subscribe(this);
        arrayAnimation = -1;

        if (Utils.cantUpdate()) return;
        onActivate();

        infoModule(name, true);
        if (mc.player != null) {
            mc.player.playSound(SoundEvent.of(Identifier.of("beta-x", "enable")),1,1);
        }
    }

    public void disable() {
        BetaX.EVENT_BUS.unsubscribe(this);

        if (Utils.cantUpdate()) return;
        onDeactivate();

        if (!this.silent) infoModule(name, false);
        if (mc.player != null) {
            mc.player.playSound(SoundEvent.of(Identifier.of("beta-x", "disable")),1,1);
        }
        this.silent = false;
    }

    protected Setting<Integer> register(final String name, final int value, final int min, final int max) {
        final Setting<Integer> s = new Setting<>(name, this, value, min, max, 1,true);
        Settings.get().addSetting(s);
        return s;
    }

    protected Setting<Double> register(final String name, final double value, final double min, final double max, final int inc) {
        final Setting<Double> s = new Setting<>(name, this, value, min, max, inc);
        Settings.get().addSetting(s);
        return s;
    }

    protected Setting<Double> register(final String name, final double value, final double min, final double max) {
        final Setting<Double> s = new Setting<>(name, this, value, min, max, 1);
        Settings.get().addSetting(s);
        return s;
    }

    protected <T>Setting<T> add(Setting setting) {
        Settings.get().addSetting(setting);
        return setting;
    }

    protected Setting<Boolean> register(final String name, final boolean value) {
        final Setting<Boolean> s = new Setting<>(name, this, value);
        Settings.get().addSetting(s);
        return s;
    }

    protected Setting<String> register(final String name, final List<String> modes, final String value) {
        final Setting<String> s = new Setting<>(name, this, modes, value);
        Settings.get().addSetting(s);
        return s;
    }


    public enum Category {
        Combat,
        Movement,
        Misc,
        Render,
        Player,
        Client,
        Lua,
        HUD
    }
    public enum chineseCategory {


    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Info {

        Module.Category category();

        String name();

        String chineseName();

        String description();

        boolean drawn() default true;

        int bind() default -1;
    }
}
