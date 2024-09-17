package dev.niuren.systems.modules.client;

import com.mojang.authlib.GameProfile;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

/**
 * @author NiuRen0827
 * Time:20:32
 */
@Module.Info(name = "FakePlayer", category = Module.Category.Client, description = "FakePlayer", chineseName = "假人")
public class FakePlayer extends Module {
    public static OtherClientPlayerEntity fakePlayer;

    private Setting<String> name = add(new Setting("Name", this,"tRollaURa_",true));
    private Setting<Double> health = add(new Setting("Health", this,20.0,0.0,20.0,1));

    @Override
    public void onActivate() {
        if (nullCheck()) {
            disable();
            return;
        }
        fakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666600"), name.get()));
        fakePlayer.setHealth(health.get().floatValue());
        fakePlayer.copyPositionAndRotation(mc.player);
        fakePlayer.getInventory().clone(mc.player.getInventory());
        mc.world.addEntity(fakePlayer);
    }


    @Override
    public void onDeactivate() {
        if (fakePlayer == null) return;
        fakePlayer.kill();
        fakePlayer.setRemoved(Entity.RemovalReason.KILLED);
        fakePlayer.onRemoved();
    }
}
