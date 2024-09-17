package dev.niuren.events.event;

import net.minecraft.item.ItemStack;

public class PickItemsEvent {
    public ItemStack itemStack;
    public int count;

    public PickItemsEvent(ItemStack itemStack, int count) {
        this.itemStack = itemStack;
        this.count = count;
    }
}
