package dev.niuren.events.event;

import dev.niuren.events.Cancelled;
import net.minecraft.client.gui.DrawContext;


public class RenderEvent extends Cancelled {

    private final DrawContext context;

    public RenderEvent(DrawContext context) {
        this.context = context;
    }

    public DrawContext getDrawContext() {
        return context;
    }

}
