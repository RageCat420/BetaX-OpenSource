package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.KeyBoardTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author NiuRen0827
 * Time:21:17
 */
@Mixin(KeyboardInput.class)
public class MixinKeyBoardInput extends Input {

    @Inject(method = {"tick"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z", shift = At.Shift.BEFORE)}, cancellable = true)
    private void hookTick$Post(boolean slowDown, float f, CallbackInfo ci) {
        KeyboardInput input = new KeyboardInput(MinecraftClient.getInstance().options);
        KeyBoardTickEvent event = new KeyBoardTickEvent((input));
        BetaX.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }

    }
}
