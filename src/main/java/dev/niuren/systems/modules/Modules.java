package dev.niuren.systems.modules;

import cn.trollaura.betax.extra.lua.LuaManager;
import cn.trollaura.betax.systems.modules.client.Component;
import cn.trollaura.betax.systems.modules.combat.NoClickDelay;
import cn.trollaura.betax.systems.modules.hud.*;
import cn.trollaura.betax.systems.modules.render.SwordBlock;
import dev.niuren.BetaX;
import dev.niuren.ic.Setting;
import dev.niuren.ic.Settings;
import dev.niuren.systems.modules.client.*;
import dev.niuren.systems.modules.combat.*;
import dev.niuren.systems.modules.misc.*;
import dev.niuren.systems.modules.movement.*;
import dev.niuren.systems.modules.player.*;
import dev.niuren.systems.modules.render.*;
import dev.niuren.systems.modules.render.nametags.Nametags;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//
public class Modules {
    private List<Module> modules;


    public void init() {
        modules = new ArrayList<>();

        addWithSettings(Component.INSTANCE);
        addWithSettings(NoClickDelay.INSTANCE);
        addWithSettings(Armor.INSTANCE);
        addWithSettings(FPS.INSTANCE);
        addWithSettings(Durability.INSTANCE);
        addWithSettings(Coords.INSTANCE);
        addWithSettings(Speed.INSTANCE);
        addWithSettings(TPS.INSTANCE);
        addWithSettings(Watermark.INSTANCE);
        addWithSettings(SwordBlock.INSTANCE);
/*        addWithSettings(Nametags.INSTANCE);*/



        // Client
        add(new ClickGUI());
        add(new HUD());
        add(new Prefix());
        add(new CombatSetting());
        add(new CombatHud());
        add(new Packet());
        add(new Exceptions());
        add(new FakePlayer());

        // Combat
        add(new Reach());
        add(new BlockLag());
        add(new Killaura());
        add(new FeetTrap());
        add(new WebAura());
        add(new AutoCrystal());
        add(new AutoTotem());
        add(new CityMiner());

        // Render
        add(new AspectRatio());
        add(new FullBright());
        add(new NoRender());
        add(new PopShader());
        add(new CrystalPlaceESP());
        add(new BlockHighLight());
        add(new PearlMarker());
        add(new HoleESP());
        add(new Nametags());
        add(new TotemParticle());
        add(new PlaceRender());


        // Player
        add(new AutoWalk());
        add(new YawLock());
        add(new AntiHunger());
        add(new AntiAim());
        add(new NoRotate());
        add(new NoFall());
        add(new AntiBadEffect());
        add(new SilentEat());
        add(new Replenish());
        add(new AutoRespawn());
        add(new AutoArmor());
        add(new ChatSuffix());
        add(new AutoPearl());
        add(new AutoSwing());
        add(new MultiTask());

        // Movement
        add(new Sprint());
        add(new NoSlow());
        add(new Velocity());
        add(new Step());
        add(new Strafe());
        add(new StrafeFix());
        add(new ClickTp());

        // Misc
        add(new LegitClick());
        add(new RaytraceBypass());
        add(new NoInteract());
        add(new AutoLog());
        add(new InvMove());
        add(new WallClip());
        add(new AutoEat());
        add(new TileBreaker());
        add(new Peek());

//        addWithSettings(Test.INSTANCE);
//        addWithSettings(Component.INSTANCE);
        LuaManager.INSTANCE.init();

        modules.sort(Comparator.comparing(Module::getName));
    }


    public void addObject(String NAME) {
        getClassesFromPackage("cn.trollaura.betax.systems.modules." + NAME).forEach(it -> {
            try {
                Object object = it.getDeclaredField("INSTANCE").get(this);

                addWithSettings((Module) object);

            } catch (
                IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<Class<?>> getClassesFromPackage(String packageName) {
        String packagePath = packageName.replace('.', '\\');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        var url = classLoader.getResource(packagePath);
        if (url == null) {
            return new ArrayList<>();
        }

        File directory;
        try {
            directory = new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            return new ArrayList<>();
        }

        List<Class<?>> classes = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            classes.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {

                        }
                    }
                }
            }
        }
        return classes;
    }//


    public static Modules get() {
        return BetaX.MODULES;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void removeAllLuas() {
        this.modules.removeIf(it -> it.getCategory() == Module.Category.Lua);
    }

    private void add(Module module) {

        if (module.getClass().isAnnotationPresent(Module.Info.class)) modules.add(module);
    }

    private void addWithSettings(Module module) {
        for (Field field : module.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if (field.get(this) instanceof Setting<?> setting) {
                    Settings.get().addSetting(setting);


                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        modules.add(module);
    }

    public List<Module> get(Module.Category category) {
        List<Module> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (module.getCategory() == category) {
                modules.add(module);
            }
        }
        return modules;
    }

    public Module get(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(clazz.getSimpleName())) return (T) module;
        }
        throw new IllegalArgumentException("Module not found!");
    }

    public boolean isActive(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(clazz.getSimpleName())) return module.isActive();
        }

        return false;
    }
}
