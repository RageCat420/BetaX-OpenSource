package dev.niuren.events.event;

import net.minecraft.entity.Entity;

public class EntityDestroyEvent {
    public Entity entity;

    public EntityDestroyEvent(Entity entity) {
        this.entity = entity;
    }
}
