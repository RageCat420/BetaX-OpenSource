package dev.niuren.systems.commands.impl;

import dev.niuren.ic.Command;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.utils.player.ChatUtils;


public class DrawnCommand extends Command {

    boolean found = false;

    public DrawnCommand() {
        super(new String[]{"d", "drawn"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            ChatUtils.info("Drawn", "Invalid input. Usage: " + getPrefix() + "drawn <Module>");
            return;
        }

        Module module = Modules.get().get(args[0]);
        if (module != null) {
            module.setDrawn(!module.drawn);
            if (module.isDrawn())
                ChatUtils.info("Drawn", module.getName() + " now drawn.");
            else
                ChatUtils.info("Drawn", module.getName() + " is no more drawn.");
        } else
            ChatUtils.info("Drawn", "Module isn't existing.");
    }

}
