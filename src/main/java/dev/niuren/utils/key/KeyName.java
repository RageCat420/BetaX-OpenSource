package dev.niuren.utils.key;

import net.minecraft.client.util.InputUtil;

public class KeyName {
    public static String get(int keyCode) {
        if (keyCode == -1) {
            return "NONE";
        }
        return InputUtil.fromKeyCode(keyCode, -1).getLocalizedText().getString();
    }
}
