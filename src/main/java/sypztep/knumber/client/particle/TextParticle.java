package sypztep.knumber.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sypztep.knumber.ModConfig;
import sypztep.knumber.client.particle.util.Easing;

import java.awt.*;

public final class TextParticle extends Particle {
    private static final int FLICK_DURATION = 12;
    private static final int FADE_DURATION = 10;
    private static final int NORMAL_GROW = 10;
    private static final int NORMAL_SHRINK = 10;
    private static final float VELOCITY_DAMPEN = 0.9f;
    private static final float FADE_AMOUNT = 0.8f;

    private String text;
    private float scale;
    private float maxSize;
    private static final Easing.ElasticOut ELASTIC_OUT = new Easing.ElasticOut();
    private float targetRed;
    private float targetGreen;
    private float targetBlue;

    public TextParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.maxAge = 20;
        this.scale = 0.0F;
        this.maxSize = -0.045F;
        this.gravityStrength = -0.125f;
        this.targetRed = this.red;
        this.targetGreen = this.green;
        this.targetBlue = this.blue;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }

    public void setColor(int red, int green, int blue) {
        this.targetRed = red / 255.0f;
        this.targetGreen = green / 255.0f;
        this.targetBlue = blue / 255.0f;
        if (ModConfig.flickParticle) {
            super.setColor(1.0f, 1.0f, 1.0f); // Start white for flick effect
        } else {
            super.setColor(targetRed, targetGreen, targetBlue); // Normal color
        }
    }

    @Override
    public void tick() {
        if (ModConfig.flickParticle) {
            if (this.age++ <= FLICK_DURATION) {
                float progress = age / (float)FLICK_DURATION;
                setColor(
                        MathHelper.lerp(progress, 1.0f, targetRed),
                        MathHelper.lerp(progress, 1.0f, targetGreen),
                        MathHelper.lerp(progress, 1.0f, targetBlue)
                );
                this.scale = MathHelper.lerp(ELASTIC_OUT.ease(progress, 0.0F, 1.0F, 1.0F), 0.0F, this.maxSize);
            } else if (this.age <= this.maxAge) {
                float progress = (age - FLICK_DURATION) / (float)FADE_DURATION;
                setColor(
                        targetRed * (1f - progress * FADE_AMOUNT),
                        targetGreen * (1f - progress * FADE_AMOUNT),
                        targetBlue * (1f - progress * FADE_AMOUNT)
                );
                this.scale = MathHelper.lerp(progress, this.maxSize, 0.0f);
            } else {
                this.markDead();
            }
            this.velocityY *= VELOCITY_DAMPEN;
        } else {
            if (this.age++ <= NORMAL_GROW) {
                float progress = age / (float)NORMAL_GROW;
                this.scale = MathHelper.lerp(ELASTIC_OUT.ease(progress, 0.0F, 1.0F, 1.0F), 0.0F, this.maxSize);
            } else if (this.age <= this.maxAge) {
                float progress = (age - NORMAL_GROW) / (float)NORMAL_SHRINK;
                this.scale = MathHelper.lerp(progress, this.maxSize, 0.0f);
            } else {
                this.markDead();
            }
        }
        super.tick();
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float particleX = (float) (prevPosX + (x - prevPosX) * tickDelta - cameraPos.x);
        float particleY = (float) (prevPosY + (y - prevPosY) * tickDelta - cameraPos.y);
        float particleZ = (float) (prevPosZ + (z - prevPosZ) * tickDelta - cameraPos.z);

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        var vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
        float textX = textRenderer.getWidth(text) / -2.0F;

        Matrix4f matrix = new Matrix4f()
                .translation(particleX, particleY, particleZ)
                .rotate(camera.getRotation())
                .rotate((float) Math.PI, 0.0F, 1.0F, 0.0F)
                .scale(scale, scale, scale);

        Vector3f offset = new Vector3f(0.0f, 0.0f, 0.03f);
        int textColor = new Color(red, green, blue, alpha).getRGB();

        for(int[] pos : new int[][]{{1,0}, {-1,0}, {0,1}, {0,-1}}) {
            matrix.translate(offset);
            textRenderer.draw(text, textX + pos[0], pos[1], 0, false, matrix,
                    vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        }

        matrix.translate(offset);
        textRenderer.draw(text, textX, 0, textColor, false, matrix,
                vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);

        vertexConsumers.draw();
    }

    @Override
    public void renderCustom(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, float tickDelta) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        render(vertexConsumer, camera, tickDelta);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }
}