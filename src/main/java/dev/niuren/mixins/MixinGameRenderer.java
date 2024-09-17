package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.Render3DEvent;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.render.AspectRatio;
import dev.niuren.systems.modules.render.NoRender;
import dev.niuren.utils.Utils;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    private float zoom;
    @Shadow
    private float zoomX;
    @Shadow
    private float zoomY;
    @Shadow
    private float viewDistance;

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderHand(MatrixStack matrixStack, Camera camera, float tickdelta, CallbackInfo info) {
        if (Utils.cantUpdate()) return;

        Render3DEvent event = new Render3DEvent(matrixStack, tickdelta);
        BetaX.EVENT_BUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "getBasicProjectionMatrix", at = @At("TAIL"), cancellable = true)
    public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
        AspectRatio aspectRatio = Modules.get().get(AspectRatio.class);

        if (aspectRatio.isActive()) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            if (zoom != 1.0f) {
                matrixStack.translate(zoomX, -zoomY, 0.0f);
                matrixStack.scale(zoom, zoom, 1.0f);
            }
            matrixStack.peek().getPositionMatrix().mul(new Matrix4f().setPerspective((float) (fov * 0.01745329238474369), aspectRatio.getSettingRatio(), 0.05f, viewDistance * 4.0f));
            cir.setReturnValue(matrixStack.peek().getPositionMatrix());
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).isActive() && Modules.get().get(NoRender.class).hurtCam.get()) {
            ci.cancel();
        }
    }
    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && Modules.get().get(NoRender.class).isActive() && Modules.get().get(NoRender.class).totem.get()) {
            info.cancel();
        }
    }

}
