package dev.niuren.systems.modules.render;

import dev.niuren.events.event.Render3DEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.render.AnimateUtil;
import dev.niuren.utils.render.ColorUtil;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
@Module.Info(name = "BlockHighLight", category = Module.Category.Render, description = "", chineseName = "方块高亮显示")

public class BlockHighLight extends Module {
    public BlockHighLight() {
        super();
    }


    private final Setting<Boolean> center
        = register("Center", true);

    private final Setting<Boolean> shrink
        = register("Shrink", true);

    private final Setting<Boolean> box
        = register("box", true);
    private final Setting<Integer> bred
        = register("boxRed", 255, 0, 255);
    private final Setting<Integer> bgreen
        = register("boxGreen", 255, 0, 255);
    private final Setting<Integer> bblue
        = register("boxBlue", 255, 0, 255);
    private final Setting<Integer> balpha
        = register("boxAlpha", 255, 0, 255);

    private final Setting<Boolean> fill
        = register("fill", true);
    private final Setting<Integer> fred
        = register("fillRed", 255, 0, 255);
    private final Setting<Integer> fgreen
        = register("fillGreen", 255, 0, 255);
    private final Setting<Integer> fblue
        = register("fillBlue", 255, 0, 255);
    private final Setting<Integer> falpha
        = register("fillAlpha", 255, 0, 255);

    private final Setting<Double> sliderSpeed
        = register("sliderSpeed", 0.2, 0.0, 1.0);
    private final Setting<Double> startFadeTime
        = register("StartFade", 0.3d, 0d, 2d);
    private final Setting<Double> fadeSpeed
        = register("FadeSpeed",  0.2d, 0.01d, 2d);

    final Timer noPosTimer = new Timer();
    static Vec3d placeVec3d;
    static Vec3d curVec3d;
    double fade = 0;

    @EventHandler//
    public void onRender3D(Render3DEvent event) {
        if(mc.crosshairTarget==null ||!(mc.crosshairTarget instanceof BlockHitResult hitResult)) return;
        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            noPosTimer.reset();
            placeVec3d = center.get() ? hitResult.getBlockPos().toCenterPos() : mc.crosshairTarget.getPos();
        }
        if (placeVec3d == null) {
            return;
        }
        if (fadeSpeed.get() >= 1) {
            fade = noPosTimer.passedMs((long) (startFadeTime.get() * 1000)) ? 0 : 0.5;
        } else {
            fade = AnimateUtil.animate(fade, noPosTimer.passedMs((long) (startFadeTime.get() * 1000)) ? 0 : 0.5, fadeSpeed.get() / 10);
        }
        if (fade == 0) {
            curVec3d = null;
            return;//
        }
        if (curVec3d == null || sliderSpeed.get() >= 1) {
            curVec3d = placeVec3d;
        } else {
            curVec3d = new Vec3d(AnimateUtil.animate(curVec3d.x, placeVec3d.x, sliderSpeed.get() / 10),
                    AnimateUtil.animate(curVec3d.y, placeVec3d.y, sliderSpeed.get() / 10),
                    AnimateUtil.animate(curVec3d.z, placeVec3d.z, sliderSpeed.get() / 10));
        }

            Box box = new Box(curVec3d, curVec3d);
            if (shrink.get()) {
                box = box.expand(fade);
            } else {
                box = box.expand(0.5);
            }
            if (fill.get()) {
                Render3DUtils.drawFill(event.matrix(), box, ColorUtil.injectAlpha(new Color(fred.get(),fgreen.get(),fblue.get()), (int) (falpha.get() * fade * 2D)));
            }
            if (this.box.get()) {
                Render3DUtils.drawBox(event.matrix(), box, ColorUtil.injectAlpha(new Color(bred.get(),bgreen.get(),bblue.get()), (int) (this.balpha.get() * fade * 2D)));
            }
        }
    }
