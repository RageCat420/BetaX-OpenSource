package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.player.InventoryUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;

/**
 * @author NiuRen0827
 * Time:20:40
 */
@Module.Info(name = "AutoEat", chineseName = "自动进食", category = Module.Category.Misc, description = "Automatically eat food.")
public class AutoEat extends Module {
    private final Setting<Double> hunger =
            register("Hunger", 10, 0, 19, 1);
    private final Setting<Double> health =
        register("Health", 20, 0, 36, 1);
    boolean eat = false;
    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        if (mc.player.getHealth() <= health.get() || mc.player.getHungerManager().getFoodLevel() <= hunger.get()) {
            if (InventoryUtil.findItem(Items.ENCHANTED_GOLDEN_APPLE) != -1) {
                InventoryUtil.switchToSlot(InventoryUtil.findItem(Items.ENCHANTED_GOLDEN_APPLE));
                mc.options.useKey.setPressed(true);
                eat = true;
            } else if (InventoryUtil.findItem(Items.GOLDEN_APPLE) != -1) {
                InventoryUtil.switchToSlot(InventoryUtil.findItem(Items.GOLDEN_APPLE));
                mc.options.useKey.setPressed(true);
                eat = true;
            }
        } else if (eat) {
            mc.options.useKey.setPressed(false);
            eat = false;
        }
    }
}
