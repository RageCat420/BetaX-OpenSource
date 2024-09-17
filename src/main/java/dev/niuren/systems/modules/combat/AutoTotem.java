package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.player.InventoryUtil;
import dev.niuren.utils.player.InventoryUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.apache.http.util.EntityUtils;

/**
 * @author NiuRen0827
 * Time:13:03
 */
@Module.Info(name = "AutoTotem", description = "AutoTotem", category = Module.Category.Combat, chineseName = "自动图腾")
public class AutoTotem extends Module {
    private final Setting<Boolean> mainHand = register("MainHand", false);
    private final Setting<Integer> health = register("Health", 10, 0, 36);
    int totems = 0;
    private final Timer timer = new Timer();

    @EventHandler
    public void onTick(TickEvent.Post event) {
        update();
    }

    private void update() {
        if (nullCheck()) return;
        totems = InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof InventoryScreen)) {
            return;
        }
        if (!timer.passedMs(200)) {
            return;
        }
        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() > health.get()) {
            return;
        }
        if (mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING || mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        int itemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING);
        if (itemSlot != -1) {
            if (mainHand.get()) {
                InventoryUtil.switchToSlot(0);
                if (mc.player.getInventory().getStack(0).getItem() != Items.TOTEM_OF_UNDYING) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, mc.player);
                    EntitiesUtil.syncInventory();
                }
            } else {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemSlot, 0, SlotActionType.PICKUP, mc.player);
                EntitiesUtil.syncInventory();
            }
            timer.reset();
        }
    }
}
