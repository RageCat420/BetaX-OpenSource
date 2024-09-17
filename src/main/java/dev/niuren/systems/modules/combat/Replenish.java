package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.timers.TimerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import static dev.niuren.utils.player.InventoryUtils.canMerge;

/**
 * @author NiuRen0827
 * Time:15:06
 */
@Module.Info(name = "Replenish", description = "Auto replenish", category = Module.Category.Combat, chineseName = "自动补给")
public class Replenish extends Module {
    private final Setting<Double> delay = register("Delay", 1, 0, 4, 1);

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (nullCheck()) return;
        if (mc.currentScreen != null) return;
        TimerUtils timer = new TimerUtils();
        if (!timer.passedMillis((long) (delay.get() * 1000))) return;
        for (int i = 0; i < 9; ++i) {
            if (replenish(i)) {
                timer.reset();
                return;
            }
        }
    }

    private boolean replenish(int slot) {
        ItemStack stack = mc.player.getInventory().getStack(slot);

        if (stack.isEmpty() || !stack.isStackable() || stack.getCount() >= 16 || stack.getCount() == stack.getMaxCount())
            return false;

        for (int i = 9; i < 36; ++i) {
            ItemStack item = mc.player.getInventory().getStack(i);
            if (item.isEmpty() || !canMerge(stack, item)) continue;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
            return true;
        }
        return false;
    }
}
