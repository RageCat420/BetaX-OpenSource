package dev.niuren.events.event;

import dev.niuren.utils.misc.Pool;
import net.minecraft.world.chunk.WorldChunk;

public class ChunkDataEvent {
    private static final Pool<ChunkDataEvent> INSTANCE = new Pool<>(ChunkDataEvent::new);

    public WorldChunk chunk;

    public ChunkDataEvent() {
    }

    public ChunkDataEvent(WorldChunk chunk) {
        INSTANCE.get().chunk = chunk;
    }

    public static void returnChunkDataEvent(ChunkDataEvent event) {
        INSTANCE.free(event);
    }
}
