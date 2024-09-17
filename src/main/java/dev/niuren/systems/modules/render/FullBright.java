package dev.niuren.systems.modules.render;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author NiuRen0827
 * Time:21:57
 */
@Module.Info(name = "FullBright", chineseName = "夜视", category = Module.Category.Render, description = "Allows you to see in the dark")
public class FullBright extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE));
    }
    @Override
    public void onDeactivate() {
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
