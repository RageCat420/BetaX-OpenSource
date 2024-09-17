package dev.niuren.mixins;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen {
    @ModifyVariable(
        method = "render",
        at = @At(value = "STORE", target = "Lnet/minecraft/SharedConstants;getGameVersion()Lnet/minecraft/SharedConstants$Version;"),
        ordinal = 0
    )
    private String modifyVersionString(String originalString) {

        return "Minecraft 1.12.2/Forge (NoModed)";
    }


}
