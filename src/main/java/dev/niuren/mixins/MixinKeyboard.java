package dev.niuren.mixins;

import dev.niuren.gui.Gui;
import dev.niuren.systems.modules.Modules;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import static dev.niuren.BetaX.mc;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {
    @Shadow
    public abstract void onKey(long window, int key, int scancode, int action, int modifiers);

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (Modules.get() == null || mc.currentScreen instanceof Gui || mc.currentScreen instanceof ChatScreen) return;
        if (action != GLFW_PRESS) return;

        Modules.get().getModules().forEach(module -> {
            if (!module.isBounded()) return;
            if (module.getBind() == key) {
                module.toggle();
            }
        });
    }
}
