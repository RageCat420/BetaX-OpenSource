package dev.niuren.systems.commands.impl;

import dev.niuren.ic.Command;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.utils.player.ChatUtils;
import net.minecraft.client.util.InputUtil;


public class BindCommand extends Command {
    public BindCommand() {
        super(new String[]{"b", "bind"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        boolean found = false;

        if (args.length == 0) {
            ChatUtils.info("Bind", "Invalid input.");
            return;
        }

        for (Module m : Modules.get().getModules()) {
            if (m.getName().equalsIgnoreCase(args[0])) {
                found = true;
                try {
                    m.setBind(InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode());
                    ChatUtils.info("Bind", m.getName() + " was bound to " + args[1].toUpperCase() + ".");
                } catch (Exception e) {
                    e.printStackTrace();
                    ChatUtils.info("Bind", "Invalid input. Usage: " + getPrefix() + "bind <Module> <Key>");
                }
                break;
            }
        }

        if (!found)
            ChatUtils.info("Bind", "Module isn't existing.");

    }

}
