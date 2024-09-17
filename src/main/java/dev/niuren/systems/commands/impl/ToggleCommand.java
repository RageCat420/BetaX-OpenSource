package dev.niuren.systems.commands.impl;

import dev.niuren.ic.Command;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;
import dev.niuren.utils.player.ChatUtils;
import net.minecraft.util.Formatting;


public class ToggleCommand extends Command {

    public ToggleCommand() {
        super(new String[]{"toggle", "t"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            ChatUtils.info("Toggle", "Invalid input! Usage: " + getPrefix() + "t <Module>");
            return;
        }

        Module module = Modules.get().get(args[0]);
        if (module != null) {
            module.toggle();
            if (module.isActive())
                ChatUtils.info("Toggle", module.getName() + " toggled " + Formatting.GREEN + " on" + Formatting.GRAY + ".");
            else
                ChatUtils.info("Toggle", module.getName() + " toggled " + Formatting.RED + " off" + Formatting.GRAY + ".");
        } else ChatUtils.info("Toggle", "Module isn't existing.");
    }
}
