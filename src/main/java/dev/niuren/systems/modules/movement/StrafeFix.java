package dev.niuren.systems.modules.movement;

import dev.niuren.events.event.JumpEvent;
import dev.niuren.events.event.PlayerMoveEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

/**
 * @author NiuRen0827
 * Time:21:32
 */
@Module.Info(name = "StrafeFix", category = Module.Category.Movement, description = "Fixes strafe when using modules that change the player's motion", chineseName = "移动修复")
public class StrafeFix extends Module {
    private float yaw;
    private float pitch;
    boolean shouldMoveFix;

    @EventHandler
    public void onStrafe(PlayerMoveEvent event) {
        if (mc.player != null && shouldMoveFix) {
            event.setYaw(yaw);
        }
    }

    @EventHandler
    public void onPlayerJump(JumpEvent event) {
        if (mc.player != null && shouldMoveFix) {
            event.setYaw(yaw);
        }
    }

    @EventHandler
    public void onMotionUpdate(dev.niuren.events.event.MotionEvent event) {
        if (mc.player != null && shouldMoveFix) {
            event.setYaw(yaw);
            event.setPitch(pitch);
            shouldMoveFix = false;
        }
    }

    public void setRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        shouldMoveFix = true;
    }
}

