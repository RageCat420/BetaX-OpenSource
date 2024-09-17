package dev.niuren.systems.commands.impl;

import dev.niuren.ic.Command;
import dev.niuren.ic.Configs;
import dev.niuren.utils.player.ChatUtils;

public class LoadCommand extends Command {

    public LoadCommand() {
        super(new String[]{"load"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        try {
            Configs.INSTANCE.load();
            ChatUtils.info("Config", "Successfully loaded.");
        } catch (Exception e) {
            ChatUtils.info("Config", "Exception while loading.");
        }
    }
}
