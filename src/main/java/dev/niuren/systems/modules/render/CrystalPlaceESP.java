package dev.niuren.systems.modules.render;

import I;
import J;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.niuren.events.event.Render3DEvent;
import dev.niuren.ic.Setting;
import dev.niuren.systems.modules.Module;
import dev.niuren.utils.math.Timer;
import dev.niuren.utils.render.Render3DUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

@Module.Info(name = "CrystalPlaceESP", chineseName = "水晶放置渲染", category = Module.Category.Render, description = "ovo")

public class CrystalPlaceESP extends Module {
    private final Setting<Boolean> range
        = register("CheckRange", true);
    private final Setting<Integer> rangeValue
        = register("Range", 12, 0, 256);
    private final Setting<Integer> red
        = register("Red", 255, 255, 255);
    private final Setting<Integer> green
        = register("Green", 255, 255, 255);
    private final Setting<Integer> blue
        = register("Blue", 255, 255, 255);
    private final Setting<Integer> animationTime
        = register("AnimationTime", 500, 0, 1500);
    private final Setting<Integer> fadeSpeed
        = register("FadeSpeed", 500, 0, 1500);
    private final Setting<Double> upSpeed
        = register("UpSpeed", 1500.0, 0.0, 3000.0);
    private final Setting<Integer> pointsNew
        = register("Points", 3, 1, 10);
    private final Setting<Integer> interval
        = register("Interval", 2, 1, 100);
    private final ConcurrentHashMap<EndCrystalEntity, RenderInfo> cryList = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    public enum Mode {
        Normal,
        New
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {

        for (Entity e : new Iterable<Entity>() {
            @Override
            public Iterator<Entity> iterator() {
                return mc.world.getEntities().iterator();
            }
        }) {
            if (!(e instanceof EndCrystalEntity)) continue;
            if (range.get() && mc.player.distanceTo(e) > rangeValue.get()) continue;
            if (!cryList.containsKey(e)) {
                cryList.put((EndCrystalEntity) e, new RenderInfo((EndCrystalEntity) e, System.currentTimeMillis()));
            }
        }


        var time = 0;
        for (int i = 0; i < pointsNew.get(); i++) {
            if (timer.passedMs(500)) {
                int finalTime = time;
                cryList.forEach((e, renderInfo) ->
                    draw(event.matrix(), renderInfo.entity, renderInfo.time - finalTime, renderInfo.time - finalTime)
                );
            }
            time += interval.get();
        }


        cryList.forEach((e, renderInfo) -> {
            if (((System.currentTimeMillis() - renderInfo.time) > animationTime.get()) && !e.isAlive()) {
                cryList.remove(e);
            }
            if (((System.currentTimeMillis() - renderInfo.time) > animationTime.get()) && mc.player.distanceTo(e) > rangeValue.get()) {
                cryList.remove(e);
            }
        });

    }

    private void draw(MatrixStack matrixStack, EndCrystalEntity entity, long radTime, long heightTime) {
        var rad = System.currentTimeMillis() - radTime;
        var height = System.currentTimeMillis() - heightTime;
        if (rad <= animationTime.get()) {
            CrystalPlaceESP.drawCircle3D(matrixStack, (Entity) entity, (float) rad / this.fadeSpeed.get(), (float) height / 1000.0F, (float) (rad / this.upSpeed.get()), new Color(red.get(), green.get(), blue.get()));
        }
    }

    public static void drawCircle3D(MatrixStack stack, Entity ent, float radius, float height, float up, Color color) {
        Render3DUtils.setupRender();
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        GL11.glLineWidth(2);
        double x = ent.prevX + (ent.getX() - ent.prevX) * mc.getTickDelta() - mc.getEntityRenderDispatcher().camera.getPos().getX();
        double y = ent.prevY + height + (ent.getY() - ent.prevY) * mc.getTickDelta() - mc.getEntityRenderDispatcher().camera.getPos().getY();
        double z = ent.prevZ + (ent.getZ() - ent.prevZ) * mc.getTickDelta() - mc.getEntityRenderDispatcher().camera.getPos().getZ();


        stack.push();
        stack.translate(x, y, z);

        Matrix4f matrix = stack.peek().getPositionMatrix();
        for (int i = 0; i <= 180; ++i) {
            bufferBuilder.vertex(matrix, (float) ((double) radius * Math.cos((double) i * 6.28 / 45.0)), up, (float) ((double) radius * Math.sin((double) i * 6.28 / 45.0))).color(color.getRGB()).next();
        }

        tessellator.draw();
        Render3DUtils.endRender();
        stack.translate(-x, -y + (double) height, -z);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        stack.pop();
    }

    @Override
    public void onDeactivate() {
        cryList.clear();
    }

    record RenderInfo(EndCrystalEntity entity, long time) {
    }

}
