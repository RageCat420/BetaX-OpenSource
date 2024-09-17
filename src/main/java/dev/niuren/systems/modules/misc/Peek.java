package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.math.Timer;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:13:55
 */
@Module.Info(name = "Peek", chineseName = "自动移动", category = Module.Category.Misc, description = "Auto peek")
public class Peek extends Module {
    private final Setting<Boolean> left = register("Left",true);
    private final Timer timer = new Timer();

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        if (left.get()){
            mc.options.leftKey.setPressed(true);
        }
        else mc.options.rightKey.setPressed(true);

    }
}
