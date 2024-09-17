package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:14:19
 */
@Module.Info(name = "Step", category = Module.Category.Movement, description = "Auto step up blocks", chineseName = "移动辅助")
public class Step extends Module {
    public Setting<Integer> stepHeight = register("StepHeight", 1, 1, 2);

    @Override
    public void onDeactivate() {
        if (mc.player == null) return;
        mc.player.setStepHeight(.5f);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        mc.player.setStepHeight(stepHeight.get());
    }
}
