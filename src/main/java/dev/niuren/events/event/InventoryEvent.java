package dev.niuren.events.event;

import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;

public class InventoryEvent {
    public InventoryS2CPacket packet;

    public InventoryEvent(InventoryS2CPacket packet) {
        this.packet = packet;
    }
}
