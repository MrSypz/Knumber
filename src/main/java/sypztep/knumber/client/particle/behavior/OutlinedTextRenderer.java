package sypztep.knumber.client.particle.behavior;

import net.minecraft.client.font.TextRenderer;
import org.joml.Vector3f;

public class OutlinedTextRenderer implements ParticleRenderer {
    private static final Vector3f OFFSET_VECTOR = new Vector3f(0.0f, 0.0f, 0.03f);
    private static final float[][] OUTLINE_OFFSETS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    @Override
    public void render(RenderContext context) {
        renderOutline(context);
        renderMainText(context);
        context.vertexConsumers().draw();
    }

    private void renderOutline(RenderContext context) {
        for (float[] offset : OUTLINE_OFFSETS) {
            context.renderMatrix().translate(OFFSET_VECTOR);
            context.textRenderer().draw(
                    context.text(),
                    context.x() + offset[0],
                    context.y() + offset[1],
                    context.outlineColor(),
                    false,
                    context.renderMatrix(),
                    context.vertexConsumers(),
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    0x000000
            );
        }
    }

    private void renderMainText(RenderContext context) {
        context.renderMatrix().translate(OFFSET_VECTOR);
        context.textRenderer().draw(
                context.text(),
                context.x(),
                context.y(),
                context.color(),
                false,
                context.renderMatrix(),
                context.vertexConsumers(),
                TextRenderer.TextLayerType.NORMAL,
                0,
                0xF000F0
        );
    }
}