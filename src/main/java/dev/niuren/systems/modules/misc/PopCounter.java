package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.DeathEvent;
import dev.niuren.events.event.TotemEvent;
import dev.niuren.ic.Command;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;

import static dev.niuren.BetaX.POP;

@Module.Info(name = "PopCounter", category = Module.Category.Misc, description = "PopCounter", chineseName = "图腾计数")

public class PopCounter extends Module {

    public static PopCounter INSTANCE;
    private final Setting<Boolean> unPop
        = register("Dead", true);

    public PopCounter() {
        super();
        INSTANCE = this;
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (POP.popContainer.containsKey(player.getName().getString())) {
            int l_Count = POP.popContainer.get(player.getName().getString());
            if (l_Count == 1) {
                if (player.equals(mc.player)) {
                    sendMessage("§fYou§r died after popping " + "§f" + l_Count + "§r totem.", player.getId());
                } else {
                    sendMessage("§f" + player.getName().getString() + "§r died after popping " + "§f" + l_Count + "§r totem.", player.getId());
                }
            } else {
                if (player.equals(mc.player)) {
                    sendMessage("§fYou§r died after popping " + "§f" + l_Count + "§r totems.", player.getId());
                } else {
                    sendMessage("§f" + player.getName().getString() + "§r died after popping " + "§f" + l_Count + "§r totems.", player.getId());
                }
            }
        } else if (unPop.get()) {
            if (player.equals(mc.player)) {
                sendMessage("§fYou§r died.", player.getId());
            } else {
                sendMessage("§f" + player.getName().getString() + "§r died.", player.getId());
            }
        }
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        int l_Count = 1;
        if (POP.popContainer.containsKey(player.getName().getString())) {
            l_Count = POP.popContainer.get(player.getName().getString());
        }
        if (l_Count == 1) {
            if (player.equals(mc.player)) {
                sendMessage("§fYou§r popped " + "§f" + l_Count + "§r totem.", player.getId());
            } else {
                sendMessage("§f" + player.getName().getString() + " §rpopped " + "§f" + l_Count + "§r totems.", player.getId());
            }
        } else {
            if (player.equals(mc.player)) {
                sendMessage("§fYou§r popped " + "§f" + l_Count + "§r totem.", player.getId());
            } else {
                sendMessage("§f" + player.getName().getString() + " §rhas popped " + "§f" + l_Count + "§r totems.", player.getId());
            }
        }
    }

    public void sendMessage(String message, int id) {
        if (!nullCheck()) {
            Command.sendClientMessage("§6[!] " + message);
        }
    }
    public enum type {
        Notify,
        Chat,
        Both
    }
}

