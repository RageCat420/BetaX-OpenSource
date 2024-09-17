package dev.niuren.systems.modules.misc;

import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * @author NiuRen0827
 * Time:14:15
 */
@Module.Info(name = "InvMove", category = Module.Category.Misc, description = "Allows you to move while in inventory", chineseName = "超限制移动")
public class InvMove extends Module {
    public Setting<Boolean> sneak = register("Sneak", false);

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.currentScreen != null) {
            if (!(mc.currentScreen instanceof ChatScreen)) {
                for (KeyBinding k : new KeyBinding[]{mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey}) {
                    k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
                }

                mc.options.forwardKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.forwardKey.getBoundKeyTranslationKey()).getCode()));

                if (sneak.get()) {
                    mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
                }
            }
        }
    }
}
