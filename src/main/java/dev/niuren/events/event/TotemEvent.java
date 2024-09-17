package dev.niuren.events.event;


import net.minecraft.entity.player.PlayerEntity;

public class TotemEvent {
    private final PlayerEntity player;

    public TotemEvent(PlayerEntity player) {
        super();
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}
