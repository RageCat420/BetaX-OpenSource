package dev.niuren.systems.modules.player;


import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.entities.EntitiesUtil;
import dev.niuren.utils.player.InventoryUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

@Module.Info(name = "AutoPearl", chineseName = "自动珍珠", category = Module.Category.Player, description = "ovo")

public class AutoPearl extends Module {
    public static AutoPearl INSTANCE;

    public AutoPearl() {
        super();
        INSTANCE = this;
    }


    private final Setting<Boolean> inventory
        = register("InventorySwap", true);
    boolean shouldThrow = false;

    @Override
    public void onActivate() {
        if (nullCheck()) {
            disable();
            return;
        }
//        if (getBind().isHoldEnable()) {
//            shouldThrow = true;
//            return;
//        }
        throwPearl(mc.player.getYaw(), mc.player.getPitch());
        disable();
    }

    public static boolean throwing = false;

    public void throwPearl(float yaw, float pitch) {
        throwing = true;
        int pearl;
        if (mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
            EntitiesUtil.sendLook(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
        } else if (inventory.get() && (pearl = InventoryUtil.findItemInventorySlot(Items.ENDER_PEARL)) != -1) {
            InventoryUtil.inventorySwap(pearl, mc.player.getInventory().selectedSlot);
            EntitiesUtil.sendLook(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
            InventoryUtil.inventorySwap(pearl, mc.player.getInventory().selectedSlot);
            EntitiesUtil.syncInventory();
        } else if ((pearl = InventoryUtil.findItem(Items.ENDER_PEARL)) != -1) {
            int old = mc.player.getInventory().selectedSlot;
            InventoryUtil.switchToSlot(pearl);
            EntitiesUtil.sendLook(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
            InventoryUtil.switchToSlot(old);
        }
        throwing = false;
    }

    @Override
    public void onDeactivate() {
        if (nullCheck()) {
            return;
        }
        if (shouldThrow) {
            shouldThrow = false;
            throwPearl(mc.player.getYaw(), mc.player.getPitch());
        }
    }
}
