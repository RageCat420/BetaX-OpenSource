package dev.niuren.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(MinecraftClient.class)
public interface IMinecraftClient {

    @Accessor("currentFps")
    static int getFps() {
        return 0;
    }
}
