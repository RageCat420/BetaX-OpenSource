package dev.niuren.systems.modules.player;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author NiuRen0827
 * Time:19:41
 */
@Module.Info(name = "AntiBadEffect", description = "", category = Module.Category.Player, chineseName = "抗坏效果")
public class AntiBadEffect extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (mc.player.hasStatusEffect(StatusEffects.NAUSEA)) mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE))
            mc.player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        if (mc.player.hasStatusEffect(StatusEffects.LEVITATION)) mc.player.removeStatusEffect(StatusEffects.LEVITATION);
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) mc.player.removeStatusEffect(StatusEffects.SLOWNESS);
    }
}
