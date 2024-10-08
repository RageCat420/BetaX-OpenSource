package dev.niuren.utils.damage;

import dev.niuren.mixins.IExplosion;
import dev.niuren.utils.block.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

import static dev.niuren.BetaX.mc;

/**
 * @author NiuRen0827
 * Time:15:46
 */
public class MeteorExplosionUtil {
    public static final Explosion explosion = new Explosion(mc.world, null, 0, 0, 0, 6f, false, Explosion.DestructionType.DESTROY);

    public static double crystalDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
        return explosionDamage(player, pos.toCenterPos().add(0, -0.5, 0), predict, 6f);
    }

    public static double crystalDamage(PlayerEntity player, Vec3d pos, PlayerEntity predict) {
        return explosionDamage(player, pos, predict, 6f);
    }

    public static double anchorDamage(PlayerEntity player, BlockPos pos, PlayerEntity predict) {
        if (BlockUtil.getBlock(pos) == Blocks.RESPAWN_ANCHOR) {
            BlockState oldState = BlockUtil.getState(pos);
            mc.world.setBlockState(pos, Blocks.AIR.getDefaultState());
            double damage = explosionDamage(player, pos.toCenterPos(), predict, 5f);
            mc.world.setBlockState(pos, oldState);
            return damage;
        } else {
            return explosionDamage(player, pos.toCenterPos(), predict, 5f);
        }
    }

    public static double explosionDamage(PlayerEntity player, Vec3d pos, PlayerEntity predict, float power) {
        if (player != null && player.getAbilities().creativeMode) return 0;
        if (predict == null) predict = player;
        double modDistance = Math.sqrt(predict.squaredDistanceTo(pos));
        if (modDistance > 10) return 0;

        double exposure = Explosion.getExposure(pos, predict);
        double impact = (1.0 - (modDistance / 10.0)) * exposure;
        double damage = (impact * impact + impact) / 2 * 7 * (5 * 2) + 1;

        // Multiply damage by difficulty
        damage = getDamageForDifficulty(damage);

        // Reduce by resistance
        damage = resistanceReduction(player, damage);

        // Reduce by armour
        damage = DamageUtil.getDamageLeft((float) damage, (float) player.getArmor(), (float) player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());

        // Reduce by enchants
        ((IExplosion) explosion).setWorld(mc.world);
        ((IExplosion) explosion).setX(pos.x);
        ((IExplosion) explosion).setY(pos.y);
        ((IExplosion) explosion).setZ(pos.z);
        ((IExplosion) explosion).setPower(power);

        damage = blastProtReduction(player, damage, explosion);

        if (damage < 0) damage = 0;
        return damage;
    }

    private static double getDamageForDifficulty(double damage) {
        return switch (mc.world.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> Math.min(damage / 2 + 1, damage);
            case HARD -> damage * 3 / 2;
            default -> damage;
        };
    }

    private static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), mc.world.getDamageSources().explosion(explosion));
        if (protLevel > 20) protLevel = 20;

        damage *= (1 - (protLevel / 25.0));
        return damage < 0 ? 0 : damage;
    }

    private static double resistanceReduction(LivingEntity player, double damage) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int lvl = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1);
            damage *= (1 - (lvl * 0.2));
        }

        return damage < 0 ? 0 : damage;
    }

}
