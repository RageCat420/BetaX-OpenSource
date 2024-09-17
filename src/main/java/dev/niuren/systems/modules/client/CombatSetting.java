package dev.niuren.systems.modules.client;

import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;

/**
 * @author NiuRen0827
 * Time:21:17
 */
@Module.Info(name = "CombatSetting", category = Module.Category.Client, description = "Combat Setting", chineseName = "战斗设置")
public class CombatSetting extends Module {
    public static CombatSetting INSTANCE = new CombatSetting();

    public final Setting<Boolean> rotate = register("Rotate", false);
    public final Setting<Boolean> invSwapBypass = register("invSwapBypass", false);
    public final Setting<Boolean> offHandCrystal = register("OffHandCrystal", false);
    public final Setting<Double> rotateTime
        = register("rotateTime", 0.5, 0, 1);
    public final Setting<Boolean> inventorySync = register("InventorySync", false);
    public final Setting<Boolean> rotateSync = register("rotateSync", false);

}
