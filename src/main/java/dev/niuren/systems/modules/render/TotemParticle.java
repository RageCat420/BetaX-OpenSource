package dev.niuren.systems.modules.render;

import dev.niuren.events.event.TotemParticleEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.render.ColorUtil;
import meteordevelopment.orbit.EventHandler;


import java.awt.*;
import java.util.Random;
@Module.Info(name = "TotemParticle", category = Module.Category.Render, chineseName = "图腾粒子渲染", description = "")

public class TotemParticle extends Module {
	public static TotemParticle INSTANCE;
    private final Setting<Integer> R
        = register("R", 255, 0, 255);
    private final Setting<Integer> G
        = register("G", 255, 0, 255);
    private final Setting<Integer> B
        = register("B", 255, 0, 255);
    private final Setting<Integer> A
        = register("A", 100, 0, 255);
    private final Setting<Integer> R2
        = register("R2", 0, 0, 255);
    private final Setting<Integer> G2
        = register("G2", 0, 0, 255);
    private final Setting<Integer> B2
        = register("B2", 0, 0, 255);
    private final Setting<Integer> A2
        = register("A2", 255, 0, 255);
    private final Setting<Integer> velocityXZ
        = register("velocityXZ", 100, 0, 500);
    private final Setting<Integer> velocityY
        = register("velocityY", 100, 0, 500);

	Random random = new Random();
	@EventHandler
	public void idk(TotemParticleEvent event) {
		event.cancel();
		event.velocityZ *= velocityXZ.get() / 100;
		event.velocityX *= velocityXZ.get() / 100;

		event.velocityY *= velocityY.get() / 100;

		event.color = ColorUtil.fadeColor(new Color(R.get(),G.get(),B.get(),A.get()), new Color(R2.get(),G2.get(),B2.get(),A2.get()), random.nextDouble());
	}
}
