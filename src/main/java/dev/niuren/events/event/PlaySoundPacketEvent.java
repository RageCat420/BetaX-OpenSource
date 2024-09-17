package dev.niuren.events.event;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

public class PlaySoundPacketEvent {
    public PlaySoundS2CPacket packet;

    public PlaySoundPacketEvent(PlaySoundS2CPacket packet) {
        this.packet = packet;
    }
}
