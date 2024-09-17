package dev.niuren.systems.modules.render;


import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.block.BlockUtil;
import dev.niuren.utils.math.FadeUtils;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.render.ColorUtil;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.HashMap;
@Module.Info(name = "PlaceRender", category = Module.Category.Render, chineseName = "放置渲染", description = "")

public class PlaceRender extends Module {
	public static PlaceRender INSTANCE;
    private final Setting<Boolean> box
        = register("box", true);

    private final Setting<Integer> boxR
        = register("boxR", 255, 0, 255);
    private final Setting<Integer> boxG
        = register("boxG", 255, 0, 255);
    private final Setting<Integer> boxB
        = register("boxB", 255, 0, 255);
    private final Setting<Integer> boxA
        = register("boxA", 100, 0, 255);
    private final Setting<Boolean> fill
        = register("fill", true);

    private final Setting<Integer> fillR
        = register("fillR", 255, 0, 255);
    private final Setting<Integer> fillG
        = register("fillG", 255, 0, 255);
    private final Setting<Integer> fillB
        = register("fillB", 255, 0, 255);
    private final Setting<Integer> fillA
        = register("fillA", 100, 0, 255);
    private final Setting<Integer> fadeTime
        = register("fadeTime",  500, 0, 3000);
    private final Setting<Integer> timeout
        = register("timeout",  500, 0, 3000);

	public PlaceRender() {
		enable();
		INSTANCE = this;
	}

	@EventHandler
	public void onRender3D(MatrixStack matrixStack, float partialTicks) {
		BlockUtil.placedPos.forEach(pos -> renderMap.put(pos, new PlacePos(pos)));
		BlockUtil.placedPos.clear();
		if (renderMap.isEmpty()) return;
		boolean shouldClear = true;
		for (PlacePos placePosition : renderMap.values()) {
			if (placePosition.isAir) {
				if (!mc.world.isAir(placePosition.pos)) {
					placePosition.isAir = false;
				} else {
					if (!placePosition.timer.passed(timeout.get())) {
						placePosition.fade.reset();
						shouldClear = false;
					}
					continue;
				}
			}
			double quads = placePosition.fade.getQuad(FadeUtils.Quad.In2);
			if (quads == 1) continue;
			shouldClear = false;
			double alpha = 1 - quads;
			double size = quads;
			Box aBox = new Box(placePosition.pos).expand(-size * 0.5, -size * 0.5, -size * 0.5);
			if (fill.get()) {
				Render3DUtils.drawFill(matrixStack, aBox, ColorUtil.injectAlpha(new Color(fillA.get(),fillG.get(),fillB.get(),fillA.get()), (int) ((double) fillA.get() * alpha)));
			}
			if (box.get()) {
				Render3DUtils.drawBox(matrixStack, aBox, ColorUtil.injectAlpha(new Color(boxR.get(),boxG.get(),boxB.get(),boxA.get()), (int) ((double) boxA.get() * alpha)));
			}
		}
		if (shouldClear) renderMap.clear();
	}

	public static final HashMap<BlockPos, PlacePos> renderMap = new HashMap<>();

	public class PlacePos {
		public final FadeUtils fade;
		public final BlockPos pos;
		public final Timer timer;
		public boolean isAir;
		public PlacePos(BlockPos placePos) {
			this.fade = new FadeUtils((long) fadeTime.get());
			this.pos = placePos;
			this.timer = new Timer();
			this.isAir = true;
		}
	}
}

