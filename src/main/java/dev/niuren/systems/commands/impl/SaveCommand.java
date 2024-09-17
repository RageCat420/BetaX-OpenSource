package dev.niuren.systems.commands.impl;

import dev.niuren.ic.Command;
import dev.niuren.ic.Configs;
import dev.niuren.utils.player.ChatUtils;

public class SaveCommand extends Command {

    public SaveCommand() {
        super(new String[]{"save"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        try {
            Configs.INSTANCE.start();
            ChatUtils.info("Config", "Successfully saved.");
        } catch (Exception e) {
            ChatUtils.info("Config", "Exception while saving.");
        }
    }
}
