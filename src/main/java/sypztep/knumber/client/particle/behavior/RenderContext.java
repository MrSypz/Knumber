package sypztep.knumber.client.particle.behavior;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

public record RenderContext(
        TextRenderer textRenderer,
        VertexConsumerProvider.Immediate vertexConsumers,
        Matrix4f renderMatrix,
        String text,
        float x,
        float y,
        int color,
        int outlineColor
) {
}
