package dev.niuren.systems.modules.player;

import dev.niuren.events.event.PacketEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:14:16
 */

@Module.Info(name = "MultiTask", category = Module.Category.Player, description = "Allows you to do multiple tasks", chineseName = "同时使用物品")
public class MultiTask extends Module {
    @EventHandler
    public void onPacket(PacketEvent.Send event){
        if(event.getPacket() instanceof net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket){
            event.cancel();
        }
    }
}
