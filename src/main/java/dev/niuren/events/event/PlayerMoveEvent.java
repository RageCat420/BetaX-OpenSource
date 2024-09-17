package dev.niuren.events.event;

public class PlayerMoveEvent {
    private float yaw;

    public PlayerMoveEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }


}
