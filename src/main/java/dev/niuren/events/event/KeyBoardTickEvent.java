package dev.niuren.events.event;

import dev.niuren.events.Cancelled;
import net.minecraft.client.input.KeyboardInput;

/**
 * @author NiuRen0827
 * Time:21:15
 */
public class KeyBoardTickEvent extends Cancelled {
    private final KeyboardInput input;

    public KeyBoardTickEvent(KeyboardInput input) {
        super();
        this.input = input;
    }

    public KeyboardInput getInput() {
        return this.input;
    }
}
