package dev.niuren.events.event;



import dev.niuren.events.Cancelled;

import java.awt.*;

public class TotemParticleEvent extends Cancelled {
    public double velocityX, velocityY, velocityZ;
    public Color color;
    public TotemParticleEvent(double velocityX, double velocityY, double velocityZ) {
        super();
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
}
