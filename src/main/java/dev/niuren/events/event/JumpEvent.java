package dev.niuren.events.event;

import dev.niuren.events.Cancelled;

/**
 * @author NiuRen0827
 * Time:13:25
 */
public class JumpEvent extends Cancelled {
    private double yaw;

    public JumpEvent(double yaw) {
        super();
        this.yaw = yaw;
    }

    public double getMotionY() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
}
