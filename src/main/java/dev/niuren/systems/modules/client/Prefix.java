package dev.niuren.systems.modules.client;

import dev.niuren.BetaX;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.player.ChatUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

@Module.Info(name = "Prefix", category = Module.Category.Client, description = "", chineseName = "前缀")
public class Prefix extends Module {
    public Setting<Boolean> prefix = register("Prefix", true);
    public Setting<Boolean> module = register("Module", true);

    public MutableText getPrefix(String message) {
        MutableText text = Text.literal("");
        text.append(Text.literal("[").setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color())));

        if (this.prefix.get()) {
            char[] chars = BetaX.NAME.toCharArray();

            List.of(chars).forEach(c -> text.append(Text.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color()))));
        } else text.append(Text.literal(BetaX.NAME).setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color())));

        text.append(Text.literal("]").setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color())));
        text.append(Text.literal(" "));

        if (ChatUtils.hasModule(message) && module.get()) {
            String[] split = message.split(" ");
            String moduleName = split[1].replace("[", "").replace("]", "");
            text.append(Text.literal("[").setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color())));
            char[] chars = moduleName.toCharArray();

            List.of(chars).forEach(c -> text.append(Text.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color()))));
            text.append(Text.literal("] ").setStyle(Style.EMPTY.withColor(HUD.INSTANCE.color())));
        }

        return text;
    }
}
