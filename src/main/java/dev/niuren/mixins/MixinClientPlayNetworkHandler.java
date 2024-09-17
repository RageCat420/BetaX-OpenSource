package dev.niuren.mixins;

import dev.niuren.BetaX;
import dev.niuren.events.event.*;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.niuren.BetaX.mc;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow
    private ClientWorld world;

    @Shadow
    public abstract void sendChatMessage(String content);

    @Unique
    private boolean ignoreChatMessage;

    @Unique
    private boolean worldNotNull;

    @Inject(method = "onPlayerRespawn", at = @At("TAIL"))
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        BetaX.EVENT_BUS.post(new PlayerRespawnEvent());
    }

/*    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
        if (packet != null && packet.getEntityType() != null) {
            if (Modules.get().get(NoRender.class).noEntity(packet.getEntityType()) && Modules.get().get(NoRender.class).getDropSpawnPacket()) {
                info.cancel();
            }
        }
    }*/

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onGameJoinHead(GameJoinS2CPacket packet, CallbackInfo info) {
        worldNotNull = world != null;
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoinTail(GameJoinS2CPacket packet, CallbackInfo info) {
        if (worldNotNull) {
            BetaX.EVENT_BUS.post(new GameLeftEvent());
        }

        BetaX.EVENT_BUS.post(new GameJoinedEvent());
    }

    @Inject(method = "onPlaySound", at = @At("HEAD"))
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo info) {
        BetaX.EVENT_BUS.post(new PlaySoundPacketEvent(packet));
    }

    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo info) {
        BetaX.EVENT_BUS.post(new ChunkDataEvent(world.getChunk(packet.getChunkX(), packet.getChunkZ())));
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("TAIL"))
    private void onContainerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo info) {
        BetaX.EVENT_BUS.post(new ContainerSlotUpdateEvent(packet));
    }

    @Inject(method = "onInventory", at = @At("TAIL"))
    private void onInventory(InventoryS2CPacket packet, CallbackInfo info) {
        BetaX.EVENT_BUS.post(new InventoryEvent(packet));
    }

    @Inject(method = "onEntitiesDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/EntitiesDestroyS2CPacket;getEntityIds()Lit/unimi/dsi/fastutil/ints/IntList;"))
    private void onEntitiesDestroy(EntitiesDestroyS2CPacket packet, CallbackInfo ci) {
        for (int id : packet.getEntityIds()) {
            BetaX.EVENT_BUS.post(new EntityDestroyEvent(world.getEntityById(id)));
        }
    }

/*    @Inject(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V", shift = At.Shift.AFTER))
    private void onExplosionVelocity(ExplosionS2CPacket packet, CallbackInfo ci) {
        Velocity velocity = Modules.get().get(Velocity.class);
        if (!velocity.explosions.get()) return;

        ((IExplosionS2CPacket) packet).setVelocityX((float) (packet.getPlayerVelocityX() * velocity.getHorizontal(velocity.explosionsHorizontal)));
        ((IExplosionS2CPacket) packet).setVelocityY((float) (packet.getPlayerVelocityY() * velocity.getVertical(velocity.explosionsVertical)));
        ((IExplosionS2CPacket) packet).setVelocityZ((float) (packet.getPlayerVelocityZ() * velocity.getHorizontal(velocity.explosionsHorizontal)));
    }*/

    @Inject(method = "onItemPickupAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getEntityById(I)Lnet/minecraft/entity/Entity;", ordinal = 0))
    private void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo info) {
        Entity itemEntity = world.getEntityById(packet.getEntityId());
        Entity entity = world.getEntityById(packet.getCollectorEntityId());

        if (itemEntity instanceof ItemEntity && entity == mc.player) {
            BetaX.EVENT_BUS.post(new PickItemsEvent(((ItemEntity) itemEntity).getStack(), packet.getStackAmount()));
        }
    }
}
