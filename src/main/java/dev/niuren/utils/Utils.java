package dev.niuren.utils;

import static dev.niuren.BetaX.mc;

public class Utils {
    public static boolean cantUpdate() {
        return mc == null || mc.player == null || mc.world == null;
    }
}
