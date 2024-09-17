package dev.niuren.systems.commands;

import cn.trollaura.betax.systems.commands.IRCCommand;
import cn.trollaura.betax.systems.commands.ReloadLuaCommand;
import dev.niuren.BetaX;
import dev.niuren.ic.Command;
import dev.niuren.systems.commands.impl.*;

import java.util.ArrayList;
import java.util.Comparator;

public class Commands {
    private final ArrayList<Command> commands;
    private boolean commandFound = false;

    public Commands() {
        commands = new ArrayList<>();

        commands.add(new BindCommand());
        commands.add(new DrawnCommand());
        commands.add(new FriendCommand());
        commands.add(new LoadCommand());
        commands.add(new MoveCommand());
        commands.add(new PrefixCommand());
        commands.add(new SaveCommand());
        commands.add(new ToggleCommand());
        commands.add(ReloadLuaCommand.INSTANCE);
        commands.add(IRCCommand.INSTANCE);

        commands.sort(Comparator.comparing(command -> command.getName()[0]));
    }

    public static Commands get() {
        return BetaX.COMMANDS;
    }

    public void runCommand(String input) {
        String[] argss = input.split(" ");
        String command = argss[0];
        String args = input.substring(command.length()).trim();

        commands.forEach(c -> {
            for (String name : c.getName()) {
                if (argss[0].equalsIgnoreCase(name)) {
                    c.onCommand(command, args.split(" "));
                    commandFound = true;
                }
            }
//            if (!commandFound) {
//                 Command.sendClientMessage(Formatting.GRAY + "Invalid input.");
//            }
        });
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
