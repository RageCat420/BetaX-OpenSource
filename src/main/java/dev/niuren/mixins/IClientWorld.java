package dev.niuren.mixins;

import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author NiuRen0827
 * Time:16:27
 */
@Mixin(ClientWorld.class)
public interface IClientWorld {
    @Accessor("pendingUpdateManager")
    PendingUpdateManager acquirePendingUpdateManager();
}
