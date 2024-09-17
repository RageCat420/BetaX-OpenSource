package dev.niuren.events.event;

import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;

public class ContainerSlotUpdateEvent {
    public ScreenHandlerSlotUpdateS2CPacket packet;

    public ContainerSlotUpdateEvent(ScreenHandlerSlotUpdateS2CPacket packet) {
        this.packet = packet;
    }
}
