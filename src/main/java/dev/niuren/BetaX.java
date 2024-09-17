package dev.niuren;


import cn.trollaura.betax.annotations.Exclude;
import cn.trollaura.betax.gui.HUDGui;
import dev.niuren.gui.Gui;
import dev.niuren.ic.Configs;
import dev.niuren.ic.Friends;
import dev.niuren.ic.Settings;
import dev.niuren.managers.PopManager;
import dev.niuren.managers.RotateManager;
import dev.niuren.systems.commands.Commands;
import dev.niuren.systems.modules.Modules;
import dev.niuren.utils.IconUtil;
import dev.niuren.utils.damage.ExplosionUtil;
import dev.niuren.utils.player.ExtrapolationUtils;
import io.netty.buffer.ByteBuf;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackPush;

public class BetaX implements ClientModInitializer {
    public static final String MOD_ID = "beta-x";
    public static final ModMetadata MOD_META;
    public static final String NAME = "BetaX";
    public static final String VERSION;

    public static BetaX INSTANCE;

    public static MinecraftClient mc;
    public static final IEventBus EVENT_BUS = new EventBus();

    public static Commands COMMANDS;
    public static Settings SETTINGS;
    public static Modules MODULES;
    public static Friends FRIENDS;
    public static Gui GUI;
    public static HUDGui HUDGUI;
    public static RotateManager ROTATE;
    public static PopManager POP;
    public static final Logger LOG;

    @Exclude
    public void sb() {
        System.out.println("\u4f60\u597d,\u964c\u751f\u4eba\uff0c\u6211\u64cd\u4f60\u5988\u4e2a\u903c\u4e86\u77e5\u9053\u5417,\u8c01\u4ed6\u5988\u53eb\u4f60\u4ed6\u5988\u53cd\u7f16\u8bd1\u8fdb\u6765\u770b\u7684\u6211\u64cd\u4f60\u5988\u903c\u4e86\u6211\u5c31\u662f\u4f60\u7239\u4e86");
    }

    @Exclude
    public void nmsl() {
        System.out.println("\u4f60\u662f\u4e0d\u662f\u6ca1\u6709\u8d2d\u4e70betax\u5c31\u60f3\u95ef\u8fdb\u6765crack\u4e86\u5144\u5f1f");
    }
    @Exclude
    public void jinMingHui() {
        System.out.println("\u4f60\u597d\u554a\u4f60\u662f\u91d1\u660e\u8f89\u5927\u5c06\u519b\u7684\u90e8\u4e0b\u5417,\u60a8\u60f3\u8981\u653b\u7834betax\u7684\u6df7\u6dc6\u5417?");
    }

    static {
        MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();
        LOG = LoggerFactory.getLogger(NAME);

        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) versionString = versionString.split("-")[0];
        if (versionString.equals("${version}")) versionString = "0.0.0";

        VERSION = versionString;
    }

    public static void setWindowIcon() {
        if (Util.getOperatingSystem() != Util.OperatingSystem.OSX) {
            try (InputStream inputStream16x = BetaX.class.getResourceAsStream("/assets/beta-x/icons/icon16x.png");
                 InputStream inputStream32x = BetaX.class.getResourceAsStream("/assets/beta-x/icons/icon32x.png")){
                ByteBuffer[] icons = new ByteBuffer[]{ IconUtil.readImageToBuffer(inputStream32x)};
                ByteBuffer icon16x = IconUtil.readImageToBuffer(inputStream16x);
                ByteBuffer icon32x = IconUtil.readImageToBuffer(inputStream32x);
                GLFWImage.Buffer iconBuffer = GLFWImage.malloc(2);

                GLFWImage icon16 = GLFWImage.malloc();
                icon16.set(16, 16, icon16x);
                iconBuffer.put(0, icon16);

                GLFWImage icon32 = GLFWImage.malloc();
                icon32.set(32, 32, icon32x);
                iconBuffer.put(1, icon32);


                GLFW.glfwSetWindowIcon(mc.getWindow().getHandle(), iconBuffer);


            }
            catch (Exception e) {
                e.printStackTrace();
                LOG.error("Couldn't set Windows Icon");
            }
        }
    }


    @Override
    public void onInitializeClient() {
        List<String> Raw = new ArrayList<>();
        try {
            URL url = new URL("https://mirror.ghproxy.com/https://raw.githubusercontent.com/tRollaURa/BetaXHwid/main/Main.txt");
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = input.readLine()) != null) {
                Raw.add(line);
            }
            String h = DigestUtils.sha256Hex(
                System.getenv("os")
                    + System.getProperty("os.name")
                    + System.getProperty("os.arch")
                    + System.getProperty("user.name")
                    + System.getenv("PROCESSOR_LEVEL")
                    + System.getenv("PROCESSOR_REVISION")
                    + System.getenv("PROCESSOR_IDENTIFIER")
                    + System.getenv("PROCESSOR_ARCHITEW6432")
            );
            if (!Raw.contains(h)) {
                System.out.println("The stupid client cracked by JinMingHui#1337");
                System.out.println("There is my github below.");
                System.out.println("https://www.github.com/");
                System.out.println("Search My QQID for getting BetaX");
                System.out.println("QQ: "+ h);
                Desktop.getDesktop().browse(new URI("https://ys.mihoyo.com/"));
                System.exit(0);
            }
        } catch (Exception s) {
            s.printStackTrace();
            System.exit(0);
        }
        if (INSTANCE == null) {
            INSTANCE = this;
        }

        LOG.info("[" + NAME + "] Initializing...");

        mc = MinecraftClient.getInstance();

        EVENT_BUS.registerLambdaFactory("dev.niuren", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        //--------------------Initialize Client--------------------//
        COMMANDS = new Commands();

        SETTINGS = new Settings();

        MODULES = new Modules();
        MODULES.init();

        FRIENDS = new Friends();
        GUI = new Gui();
        HUDGUI = HUDGui.INSTANCE;
/*        try {
            GChat.get().startClient();

            GChat.get().startReconnectCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Configs CONFIGS = new Configs();

        EVENT_BUS.subscribe(ExplosionUtil.class);
        EVENT_BUS.subscribe(ExtrapolationUtils.class);

        //--------------------Pre Load--------------------//
        if (!CONFIGS.MAIN_FOLDER.exists()) {
            CONFIGS.addPreLoadTask(() -> {
                // 第一次启动游戏执行的东西
            });
        }

        CONFIGS.load();


        LOG.info("[" + NAME + "] Loaded Successfully!");
        Runtime.getRuntime().addShutdownHook(new Configs());
    }
}
