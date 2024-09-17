package dev.niuren.events.event;


import dev.niuren.events.Cancelled;
import net.minecraft.network.packet.Packet;

public class PacketEvent extends Cancelled {

    public final Packet<?> packet;
    public PacketEvent(Packet<?> packet) {
        super();
        this.packet = packet;
    }
    public <T extends Packet<?>> T getPacket() {
        return (T) packet;
    }
    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }

    }
}
