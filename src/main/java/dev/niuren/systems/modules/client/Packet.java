package dev.niuren.systems.modules.client;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;

/**
 * @author NiuRen0827
 * Time:15:38
 */
@Module.Info(name = "PacketLogger", description = "Packets setting", category = Module.Category.Client, chineseName = "发包输出")
public class Packet extends Module {
    public ArrayList<net.minecraft.network.packet.Packet<?>> packet = new ArrayList<>();

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        packet.add(event.packet);
    }
}
