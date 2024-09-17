package dev.niuren.mixins;

import dev.niuren.BetaX;
import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tRollaURa_
 */
@Mixin(Window.class)
public class MixinWindows {
    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Icons;getIcons(Lnet/minecraft/resource/ResourcePack;)Ljava/util/List;"))
    public List<InputSupplier<InputStream>> setI(Icons instance, ResourcePack resourcePack) {
        InputStream inputStream16x = BetaX.class.getResourceAsStream("/assets/beta-x/icons/icon16x.png");
        InputStream inputStream32x = BetaX.class.getResourceAsStream("/assets/beta-x/icons/icon32x.png");
         return List.of(() -> inputStream16x, () -> inputStream32x);
    }
}
