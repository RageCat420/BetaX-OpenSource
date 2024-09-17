package dev.niuren.systems.modules.render;

import dev.niuren.events.event.ParticleEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.particle.TotemParticle;

/**
 * @author NiuRen0827
 * Time:19:33
 */
@Module.Info(name = "PopShader", category = Module.Category.Render, chineseName = "图腾渲染", description = "")
public class PopShader extends Module {
    private final Setting<Integer> red = register("Reg", 255, 0, 255);
    private final Setting<Integer> green = register("Green", 255, 0, 255);
    private final Setting<Integer> blue = register("Blue", 255, 0, 255);

    @EventHandler
    public void onParticle(ParticleEvent.AddParticle event) {
        if (event.particle instanceof TotemParticle) {
            event.particle.setColor(red.get(), green.get(), blue.get());
        }
    }
}
