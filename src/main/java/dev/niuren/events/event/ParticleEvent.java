package dev.niuren.events.event;

import dev.niuren.events.Cancelled;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;

/**
 * @author NiuRen0827
 * Time:19:08
 */
public class ParticleEvent extends Cancelled {
    public ParticleEvent() {
    }

    public static class AddParticle extends ParticleEvent {

        public final Particle particle;

        public AddParticle(Particle particle) {
            this.particle = particle;
        }

    }

    public static class AddEmmiter extends ParticleEvent {
        public final ParticleEffect emmiter;

        public AddEmmiter(ParticleEffect emmiter) {
            this.emmiter = emmiter;
        }

    }
}
