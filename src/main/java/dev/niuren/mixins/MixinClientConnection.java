package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.PacketEvent;
import dev.niuren.events.event.SendMessageEvent;
import dev.niuren.ic.Command;
import dev.niuren.systems.commands.Commands;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ChatMessageC2SPacket pack) {
            if (pack.chatMessage().startsWith(Command.getPrefix())) {
                Commands.get().runCommand(pack.chatMessage().substring(Command.getPrefix().length()));
                ci.cancel();
            }
            SendMessageEvent event = new SendMessageEvent(pack.chatMessage());
            BetaX.EVENT_BUS.post(event);
        }
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        PacketEvent.Receive event = new PacketEvent.Receive(packet);
        BetaX.EVENT_BUS.post(event);

        if (event.isCancelled()) info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        BetaX.EVENT_BUS.post(event);

        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("TAIL"), cancellable = true)
    private void onSendPacketTail(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        BetaX.EVENT_BUS.post(event);

        if (event.isCancelled()) info.cancel();
    }
}
