package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

/**
 * @author NiuRen0827
 * Time:11:46
 */
@Module.Info(name = "AutoLog", category = Module.Category.Misc, description = "", chineseName = "自动退出")
public class AutoLog extends Module {
    private final Setting<Integer> health = register("Health", 10, 0, 20);

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (nullCheck()) return;
        if (mc.player.getHealth() <= health.get()) {
            disconnect();
            disable();
        }
    }

    private void disconnect() {
        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("[AutoLog] current health are not support")));
    }
}
