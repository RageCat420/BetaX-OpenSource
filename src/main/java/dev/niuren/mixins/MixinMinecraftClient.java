package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.TickEvent;
import dev.niuren.gui.font.FontRenderers;
import dev.niuren.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Unique
    private boolean doItemUseCalled;
    @Unique
    private boolean rightClick;

    @Shadow
    protected abstract void doItemUse();

    @Shadow
    public abstract Profiler getProfiler();

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    public abstract void close();

    @Inject(method = "<init>", at = @At("TAIL"))
    void postWindowInit(RunArgs args, CallbackInfo ci) {
        try {
            System.out.println("\u6211\u5c31\u662f\u5927\u795e\u725b\u4eba,\u4fe1\u4e0d\u4fe1\u6211\u7528betax\u76f4\u63a5\u62bd\u6253\u4f60");

            FontRenderers.Arial = FontRenderers.createDefault(20f,"bhc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    private void modifyWindowTitleString(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("吉吉岛");
    }



    @Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
        if (Utils.cantUpdate()) return;

        doItemUseCalled = false;


        getProfiler().push(BetaX.MOD_ID + "_pre_update");
        BetaX.EVENT_BUS.post(TickEvent.Pre.get());
        getProfiler().pop();

        if (rightClick && !doItemUseCalled && interactionManager != null) doItemUse();
        rightClick = false;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo info) {

        if (Utils.cantUpdate()) return;


        getProfiler().push(BetaX.MOD_ID + "_post_update");
        BetaX.EVENT_BUS.post(TickEvent.Post.get());
        getProfiler().pop();
    }

}
