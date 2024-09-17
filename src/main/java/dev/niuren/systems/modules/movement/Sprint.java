package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.Arrays;

@Module.Info(name = "Sprint", category = Module.Category.Movement, description = "Automatically sprints.", chineseName = "自动疾跑")
public class Sprint extends Module {
    public Setting<String> mode = register("SpeedMode", Arrays.asList("Strict", "Rage"), "Strict");

    @Override
    public void onDeactivate() {
        mc.player.setSprinting(false);
    }

    private void sprint() {
        if (mc.player.getHungerManager().getFoodLevel() <= 6) return;
        mc.player.setSprinting(true);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case "Strict" -> {
                if (mc.player.forwardSpeed > 0) {
                    sprint();
                }
            }
            case "Rage" -> sprint();
        }
    }
}
