package dev.niuren.events.event;


import net.minecraft.entity.player.PlayerEntity;

public class DeathEvent  {
    private final PlayerEntity player;

    public DeathEvent(PlayerEntity player) {
        super();
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}
