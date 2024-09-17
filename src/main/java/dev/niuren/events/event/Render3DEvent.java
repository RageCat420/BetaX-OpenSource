package dev.niuren.events.event;

import dev.niuren.events.Cancelled;
import dev.niuren.utils.render.Render3DUtils;
import net.minecraft.client.util.math.MatrixStack;


public class Render3DEvent extends Cancelled implements Render3DUtils {

    private final float partialTicks;
    private final MatrixStack matrixStack;

    public Render3DEvent(MatrixStack matrixStack, float partialTicks) {
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
    }

    public float partial() {
        return partialTicks;
    }

    public MatrixStack matrix() {
        return matrixStack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public static class EventRender3DNoBob extends Cancelled {

        private final float partialTicks;
        private final MatrixStack matrixStack;

        public EventRender3DNoBob(MatrixStack matrixStack, float partialTicks2) {
            this.partialTicks = partialTicks2;
            this.matrixStack = matrixStack;
        }

        public float getPartialTicks() {
            return partialTicks;
        }

        public MatrixStack getMatrixStack() {
            return matrixStack;
        }
    }
}
