package dev.niuren.systems.modules.render;

import dev.niuren.clion.animations.Easing;
import dev.niuren.clion.animations.plus.EasingSingle;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;

@Module.Info(category = Module.Category.Render, name = "AspectRatio", chineseName = "自定义视角", description = "Pull your minecraft screen.")
public class AspectRatio extends Module {
    private final Setting<Boolean> smooth = register("Smooth", true);
    private final Setting<Double> ratio = register("Ratio", 1.78, 0.1, 8.0);

    private final EasingSingle easingSingle = new EasingSingle(1.78f, 1500f, Easing.OUT_SINE);

    @Override
    public void onDeactivate() {
        easingSingle.forceUpdatePos(1.78f);
    }

    public float getSettingRatio() {
        if (smooth.get()) {
            easingSingle.updatePos(ratio.get().floatValue());
            return easingSingle.getUpdate();
        }
        return ratio.get().floatValue();
    }
}
