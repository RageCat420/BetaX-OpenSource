package dev.niuren.systems.modules.combat;

import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;

/**
 * @author NiuRen0827
 * Time:11:41
 */
@Module.Info(name = "Reach", category = Module.Category.Combat, description = "", chineseName = "攻击距离")
public class Reach extends Module {
    public final Setting<Double> reach = register("Reach", 5.0, 3.0, 10.0);
}
