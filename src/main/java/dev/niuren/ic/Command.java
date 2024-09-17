package dev.niuren.ic;

import dev.niuren.BetaX;
import dev.niuren.mixininterface.IChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static dev.niuren.BetaX.mc;

public class Command {
    private static String prefix = ".";

    private final String[] name;

    public Command(String[] command) {
        this.name = command;
    }

    public static void sendClientMessage(String string) {
        ((IChatHud) mc.inGameHud.getChatHud()).add(Text.literal("" + Formatting.RED + "[" + BetaX.NAME + "] " + string), 86741);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        Command.prefix = prefix;
    }

    public String[] getName() {
        return name;
    }

    public void onCommand(String command, String[] args) {
    }
}
