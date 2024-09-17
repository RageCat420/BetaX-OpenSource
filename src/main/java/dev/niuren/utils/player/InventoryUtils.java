package dev.niuren.utils.player;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import java.util.HashMap;
import java.util.Map;

import static dev.niuren.systems.modules.Module.mc;

/**
 * @author NiuRen0827
 * Time:14:14
 */
public class InventoryUtils {
    public static void doSwap(int slot) {
        if (slot != -1) {
            if (slot != mc.player.getInventory().selectedSlot) {
                mc.player.getInventory().selectedSlot = slot;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            }
        }
    }

    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = getStackInSlot(i).getItem();
            if (Item.getRawId(item) == Item.getRawId(input)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean canMerge(ItemStack source, ItemStack stack) {
        return source.getItem() == stack.getItem() && source.getName().equals(stack.getName());
    }

    public static ItemStack getStackInSlot(int i) {
        return i == -1 ? null : mc.player.getInventory().getStack(i);
    }

    public static int getItemCount(Item item) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item) continue;
            count = count + entry.getValue().getCount();
        }
        return count;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, mc.player.getInventory().getStack(current));
        }

        return fullInventorySlots;
    }
}
