package dev.niuren.mixins;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author NiuRen0827
 * Time:14:31
 */
@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {

    @Mutable
    @Accessor("playerVelocityX")
    void setX(float velocityX);

    @Mutable
    @Accessor("playerVelocityY")
    void setY(float velocityY);

    @Mutable
    @Accessor("playerVelocityZ")
    void setZ(float velocityZ);

    @Accessor("playerVelocityX")
    float getX();

    @Accessor("playerVelocityY")
    float getY();

    @Accessor("playerVelocityZ")
    float getZ();
}
