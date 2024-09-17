package dev.niuren.mixins;

import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.combat.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author NiuRen0827
 * Time:11:39
 */
@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(at = {@At("HEAD")}, method = {"getReachDistance()F"}, cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> ci) {
        if (Modules.get().isActive(Reach.class)) {
            ci.setReturnValue(Modules.get().get(Reach.class).reach.get().floatValue());
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"hasExtendedReach()Z"}, cancellable = true)
    private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().isActive(Reach.class))
            cir.setReturnValue(true);
    }
}
