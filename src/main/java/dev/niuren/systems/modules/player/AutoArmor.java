package dev.niuren.systems.modules.player;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.player.MovementUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NiuRen0827
 * Time:19:33
 */
@Module.Info(name = "AutoArmor", description = "", category = Module.Category.Player, chineseName = "自动穿甲")
public class AutoArmor extends Module {
    private int tickDelay = 0;
    public final Setting<Boolean> noMove = register("NoMove", true);
    public final Setting<Integer> delay = register("Delay", 5, 1, 10);


    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (mc.player.playerScreenHandler != mc.player.currentScreenHandler)
            return;

        if (MovementUtil.isMoving() && noMove.get()) return;

        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        tickDelay = delay.get();

        Map<EquipmentSlot, int[]> armorMap = new HashMap<>(4);
        armorMap.put(EquipmentSlot.FEET, new int[]{36, getProtection(mc.player.getInventory().getStack(36)), -1, -1});
        armorMap.put(EquipmentSlot.LEGS, new int[]{37, getProtection(mc.player.getInventory().getStack(37)), -1, -1});
        armorMap.put(EquipmentSlot.CHEST, new int[]{38, getProtection(mc.player.getInventory().getStack(38)), -1, -1});
        armorMap.put(EquipmentSlot.HEAD, new int[]{39, getProtection(mc.player.getInventory().getStack(39)), -1, -1});

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            int prot = getProtection(stack);
            if (prot > 0) {
                for (Map.Entry<EquipmentSlot, int[]> e : armorMap.entrySet()) {
                    if (e.getKey() == (stack.getItem() instanceof ElytraItem ? EquipmentSlot.CHEST : ((ArmorItem) stack.getItem()).getSlotType())) {
                        if (prot > e.getValue()[1] && prot > e.getValue()[3]) {
                            e.getValue()[2] = i;
                            e.getValue()[3] = prot;
                        }
                    }
                }
            }
        }

        for (Map.Entry<EquipmentSlot, int[]> e : armorMap.entrySet()) {
            if (e.getValue()[2] != -1) {
                if (e.getValue()[1] == -1 && e.getValue()[2] < 9) {
                    if (e.getValue()[2] != mc.player.getInventory().selectedSlot) {
                        mc.player.getInventory().selectedSlot = e.getValue()[2];
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(e.getValue()[2]));
                    }
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + e.getValue()[2], 1, SlotActionType.QUICK_MOVE, mc.player);
                    mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                } else if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
                    int armorSlot = (e.getValue()[0] - 34) + (39 - e.getValue()[0]) * 2;
                    int newArmorslot = e.getValue()[2] < 9 ? 36 + e.getValue()[2] : e.getValue()[2];
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, newArmorslot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, armorSlot, 0, SlotActionType.PICKUP, mc.player);
                    if (e.getValue()[1] != -1)
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, newArmorslot, 0, SlotActionType.PICKUP, mc.player);
                    mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                }
                return;
            }
        }
    }

    private int getProtection(ItemStack is) {
        if (is.getItem() instanceof ArmorItem || is.getItem() instanceof ElytraItem) {
            int prot = 0;

            if (is.getItem() instanceof ElytraItem) {
                if (!ElytraItem.isUsable(is))
                    return 0;
                prot = 1;
            }
            if (is.hasEnchantments()) {
                for (Map.Entry<Enchantment, Integer> e : EnchantmentHelper.get(is).entrySet()) {
                    if (e.getKey() instanceof ProtectionEnchantment)
                        prot += e.getValue();
                }
            }
            return (is.getItem() instanceof ArmorItem ? ((ArmorItem) is.getItem()).getProtection() : 0) + prot;
        } else if (!is.isEmpty()) {
            return 0;
        }

        return -1;
    }
}
