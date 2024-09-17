package dev.niuren.systems.modules.combat;

import dev.niuren.events.event.GameLeftEvent;
import dev.niuren.events.event.TickEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;

import static dev.niuren.utils.block.BlockUtils.isInsideBlock;
import static dev.niuren.utils.block.BlockUtils.placeBlock;

/**
 * @author NiuRen0827
 * Time:20:32
 */
@Module.Info(name = "BlockLag", category = Module.Category.Combat, description = "BlockLag", chineseName = "卡方块")
public class BlockLag extends Module {
    private final Setting<Boolean> rotate = register("Rotate", false);
    private final Setting<Boolean> autoDisable = register("AutoDisable", false);
    private final Setting<Boolean> attack = register("Attack", false);

    private double prevY;

    public void onEnable() {
        if (mc.player != null) {
            this.prevY = mc.player.getY();
        }
    }

    @EventHandler
    public void onDisconnect(GameLeftEvent event) {
        this.disable();
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        BlockPos pos = mc.player.getBlockPos();
        if (!isInsideBlock()) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.42D, mc.player.getZ(), true));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.75D, mc.player.getZ(), true));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.01D, mc.player.getZ(), true));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.16D, mc.player.getZ(), true));
            mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.167D, mc.player.getZ());
            if (attack.get()) {
                breakCrystal(pos);
            }
            placeBlock(pos);
            mc.player.setPosition(mc.player.getX(), mc.player.getY() - 1.167D, mc.player.getZ());
            Vec3d dist = this.getLagOffsetVec();
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(dist.getX(), dist.getY(), dist.getZ(), false));
        }

        if (autoDisable.get()) {
            this.disable();
        }
    }


    public void breakCrystal(BlockPos pos) {
        Hand hand = Hand.MAIN_HAND;
        Iterator<EndCrystalEntity> endCrystalEntityIterator = mc.world.getNonSpectatingEntities(EndCrystalEntity.class, new Box((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1))).iterator();
        if (endCrystalEntityIterator.hasNext()) {
            EndCrystalEntity crystal = endCrystalEntityIterator.next();
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
            mc.world.removeEntity(crystal.getId(), Entity.RemovalReason.KILLED);

            mc.player.attack(crystal);
            mc.player.swingHand(hand);
        }
    }

    public Vec3d getLagOffsetVec() {
        return new Vec3d(mc.player.getX(), mc.player.getY() + 3.5D, mc.player.getZ());
    }
}
