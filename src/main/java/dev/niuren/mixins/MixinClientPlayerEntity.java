package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.MotionEvent;
import dev.niuren.events.event.MoveEvent;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.movement.NoSlow;
import dev.niuren.systems.modules.movement.Velocity;
import dev.niuren.utils.player.MovementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author NiuRen0827
 * Time:11:36
 */
@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    @Shadow
    protected abstract void sendSprintingPacket();

    @Shadow
    private boolean lastSneaking;

    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    protected abstract boolean isCamera();

    @Shadow
    private double lastX;

    @Shadow
    private double lastBaseY;

    @Shadow
    private double lastZ;

    @Shadow
    private float lastYaw;

    @Shadow
    private float lastPitch;

    @Shadow
    private int ticksSinceLastPositionPacketSent;

    @Shadow
    private boolean lastOnGround;

    @Shadow
    private boolean autoJumpEnabled;

    @Shadow
    @Final
    protected MinecraftClient client;

    public MixinClientPlayerEntity() {
        super(null, null);
    }

    /**
     * @author niuren
     * @reason for rotate
     */
    @Overwrite
    private void sendMovementPackets() {

        MotionEvent event = new MotionEvent(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), this.isOnGround());
        BetaX.EVENT_BUS.post(event);
        this.sendSprintingPacket();
        boolean bl = this.isSneaking();
        if (bl != this.lastSneaking) {
            ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSneaking = bl;
        }

        if (this.isCamera()) {
            MovementUtil.lastYaw = lastYaw;
            MovementUtil.lastPitch = lastPitch;
            double d = event.getX() - this.lastX;
            double e = event.getY() - this.lastBaseY;
            double f = event.getZ() - this.lastZ;
            double g = (double) (event.getYaw() - this.lastYaw);
            double h = (double) (event.getPitch() - this.lastPitch);
            ++this.ticksSinceLastPositionPacketSent;
            boolean bl2 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl3 = g != 0.0 || h != 0.0;
            if (this.hasVehicle()) {
                Vec3d vec3d = this.getVelocity();
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, event.getYaw(), event.getPitch(), event.isOnGround()));
                bl2 = false;
            } else if (bl2 && bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (this.lastOnGround != this.isOnGround()) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(event.isOnGround()));
            }

            if (bl2) {
                this.lastX = event.getX();
                this.lastBaseY = event.getY();
                this.lastZ = event.getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }

            if (bl3) {
                this.lastYaw = event.getYaw();
                this.lastPitch = event.getPitch();
                MovementUtil.lastYaw = lastYaw;
                MovementUtil.lastPitch = lastPitch;
            }

            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = (Boolean) this.client.options.getAutoJump().getValue();
        }

    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean tickMovementHook(ClientPlayerEntity player) {
        if (Modules.get().isActive(NoSlow.class))
            return false;
        return player.isUsingItem();
    }
    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
        if (Modules.get().get(Velocity.class).blockPush.get() && Modules.get().get(Velocity.class).isActive()) {
            info.cancel();
        }
    }


    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
    public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
        BetaX.EVENT_BUS.post(event);
        ci.cancel();
        super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));

    }

    @Unique
    public float getLastYaw() {
        return this.lastYaw;
    }

    @Unique
    public float getLastPitch() {
        return this.lastPitch;
    }
}
