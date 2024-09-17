package dev.niuren.systems.modules.render;

import dev.niuren.events.event.TickEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.text.Text;

/**
 * @author NiuRen0827
 * Time:20:51
 */
@Module.Info(name = "PearlMarker", chineseName = "末影珍珠标记", category = Module.Category.Render, description = "Shows the nametag of the pearl.")
public class PearlMarker extends Module {
    @EventHandler
    public void onTick(TickEvent.Post event){
        for (Entity entity : mc.world.getEntities()){
            if (entity instanceof EnderPearlEntity){
                entity.setCustomName(Text.of(entity.getUuidAsString()));
                entity.setCustomNameVisible(true);
            }
        }
    }
}
