package cn.trollaura.betax.util

import dev.niuren.systems.modules.Modules
import dev.niuren.systems.modules.combat.Killaura
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ShieldItem
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand

object SwordBlockUtil {
    @JvmStatic
    fun isWeaponBlocking(entity: LivingEntity): Boolean {
        if(Killaura.get().isActive && Killaura.get().blockModeSetting.get() == "Fake" && Killaura.get().target != null &&  MinecraftClient.getInstance().player!!.getStackInHand(Hand.MAIN_HAND).item is SwordItem) {
            return true
        }

        return (MinecraftClient.getInstance().options.useKey.isPressed || isBlocking(entity)) && MinecraftClient.getInstance().player!!.getStackInHand(Hand.MAIN_HAND).item is SwordItem

    }

    @JvmStatic
    fun isBlocking(entity: LivingEntity): Boolean {
        return entity.isBlocking
    }
}
