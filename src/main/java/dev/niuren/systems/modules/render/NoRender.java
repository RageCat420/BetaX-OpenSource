package dev.niuren.systems.modules.render;

import dev.niuren.events.event.ParticleEvent;
import dev.niuren.events.event.Render3DEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.particle.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;

/**
 * @author NiuRen0827
 * Time:19:11
 */
@Module.Info(name = "NoRender", category = Module.Category.Render, chineseName = "不渲染", description = "")
public class NoRender extends Module {
    public static NoRender INSTANCE = new NoRender();
    private final Setting<Boolean> potions = register("Potions", false);
    private final Setting<Boolean> xp = register("XP", false);
    private final Setting<Boolean> arrows = register("Arrows", false);
    private final Setting<Boolean> eggs = register("Eggs", false);
    private final Setting<Boolean> item = register("Item", false);
    private final Setting<Boolean> elderGuardian = register("ElderGuardian", false);
    public final Setting<Boolean> hurtCam = register("HurtCam", false);
    private final Setting<Boolean> explosions = register("Explosions", false);
    private final Setting<Boolean> campFire = register("CampFire", false);
    private final Setting<Boolean> fireworks = register("Fireworks", false);
    private final Setting<Boolean> effect = register("Effect", false);
    public final Setting<Boolean> fireOverlay = register("fireOverlay", false);
    public final Setting<Boolean> waterOverlay = register("waterOverlay", false);
    public final Setting<Boolean> blockOverlay = register("blockOverlay", false);
    public final Setting<Boolean> totem = register("totem", false);

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        for (Entity ent : mc.world.getEntities()) {
            if (ent instanceof PotionEntity) {
                if (potions.get())
                    mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof ExperienceBottleEntity) {
                if (xp.get())
                    mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof ArrowEntity) {
                if (arrows.get())
                    mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if (ent instanceof EggEntity) {
                if (eggs.get())
                    mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
            if(ent instanceof ItemEntity){
                if(item.get())
                    mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
            }
        }
    }

    @EventHandler
    public void onParticle(ParticleEvent.AddParticle event) {
        if (elderGuardian.get() && event.particle instanceof ElderGuardianAppearanceParticle) {
            event.setCancelled(true);
        } else if (explosions.get() && event.particle instanceof ExplosionLargeParticle) {
            event.setCancelled(true);
        } else if (campFire.get() && event.particle instanceof CampfireSmokeParticle) {
            event.setCancelled(true);
        } else if (fireworks.get() && (event.particle instanceof FireworksSparkParticle.FireworkParticle || event.particle instanceof FireworksSparkParticle.Flash)) {
            event.setCancelled(true);
        } else if (effect.get() && event.particle instanceof SpellParticle) {
            event.cancel();
        }
    }
}
