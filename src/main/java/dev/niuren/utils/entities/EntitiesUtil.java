package dev.niuren.utils.entities;

import dev.niuren.BetaX;
import dev.niuren.mixins.IEntityTrackingSection;
import dev.niuren.mixins.ISectionedEntityCache;
import dev.niuren.mixins.ISimpleEntityLookup;
import dev.niuren.mixins.IWorld;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.CombatSetting;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static dev.niuren.systems.modules.Module.mc;

/**
 * @author NiuRen0827
 * Time:8:36
 */
public class EntitiesUtil {
    public static List<PlayerEntity> getEnemies(double range) {
        List<PlayerEntity> list = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (mc.player.distanceTo(player) > range) continue;
            list.add(player);
        }
        return list;
    }
    public static boolean rotating = false;

    public static Vec3d getEyesPos() {
        return mc.player.getEyePos();
    }
    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.getYaw() + MathHelper.wrapDegrees(yaw - mc.player.getYaw()), mc.player.getPitch() + MathHelper.wrapDegrees(pitch - mc.player.getPitch())};
    }
    public static void sendLook(PlayerMoveC2SPacket packet) {
        if (BetaX.ROTATE == null) return;
        if (!packet.changesLook() || packet.getYaw(114514) == BetaX.ROTATE.lastYaw && packet.getPitch(114514) == BetaX.ROTATE.lastPitch) {
            return;
        }
        rotating = true;
        BetaX.ROTATE.setRotation(packet.getYaw(0), packet.getPitch(0), true);
        mc.player.networkHandler.sendPacket(packet);
        rotating = false;
    }
    public static void syncInventory() {
        if (Modules.get().get(CombatSetting.class).inventorySync.get())
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
    }

    public static PlayerEntity getClosestEnemy(double distance) {
        PlayerEntity closest = null;

        for (PlayerEntity player : getEnemies(distance)) {
            if (closest == null) {
                closest = player;
                continue;
            }

            if (!(mc.player.getEyePos().squaredDistanceTo(player.getPos()) < mc.player.squaredDistanceTo(closest)))
                continue;

            closest = player;
        }
        return closest;
    }

    public static boolean intersectsWithEntity(Box box, Predicate<Entity> predicate, Map<AbstractClientPlayerEntity, Box> customBoxes) {
        EntityLookup<Entity> entityLookup = ((IWorld) mc.world).getEntityLookup();

        // Fast implementation using SimpleEntityLookup that returns on the first intersecting entity
        if (entityLookup instanceof SimpleEntityLookup<Entity> simpleEntityLookup) {
            SectionedEntityCache<Entity> cache = ((ISimpleEntityLookup) simpleEntityLookup).getCache();
            LongSortedSet trackedPositions = ((ISectionedEntityCache) cache).getTrackedPositions();
            Long2ObjectMap<EntityTrackingSection<Entity>> trackingSections = ((ISectionedEntityCache) cache).getTrackingSections();

            int i = ChunkSectionPos.getSectionCoord(box.minX - 2);
            int j = ChunkSectionPos.getSectionCoord(box.minY - 2);
            int k = ChunkSectionPos.getSectionCoord(box.minZ - 2);
            int l = ChunkSectionPos.getSectionCoord(box.maxX + 2);
            int m = ChunkSectionPos.getSectionCoord(box.maxY + 2);
            int n = ChunkSectionPos.getSectionCoord(box.maxZ + 2);

            for (int o = i; o <= l; o++) {
                long p = ChunkSectionPos.asLong(o, 0, 0);
                long q = ChunkSectionPos.asLong(o, -1, -1);
                LongBidirectionalIterator longIterator = trackedPositions.subSet(p, q + 1).iterator();

                while (longIterator.hasNext()) {
                    long r = longIterator.nextLong();
                    int s = ChunkSectionPos.unpackY(r);
                    int t = ChunkSectionPos.unpackZ(r);

                    if (s >= j && s <= m && t >= k && t <= n) {
                        EntityTrackingSection<Entity> entityTrackingSection = trackingSections.get(r);

                        if (entityTrackingSection != null && entityTrackingSection.getStatus().shouldTrack()) {
                            for (Entity entity : ((IEntityTrackingSection) entityTrackingSection).<Entity>getCollection()) {
                                if ((entity instanceof PlayerEntity && customBoxes.containsKey(entity) ? customBoxes.get(entity) : entity.getBoundingBox()).intersects(box) && predicate.test(entity))
                                    return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
        // Slow implementation that loops every entity if for some reason the EntityLookup implementation is changed
        AtomicBoolean found = new AtomicBoolean(false);

        entityLookup.forEachIntersects(box, entity -> {
            if (!found.get() && predicate.test(entity)) found.set(true);
        });

        return found.get();
    }
}
