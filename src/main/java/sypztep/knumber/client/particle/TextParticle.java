package sypztep.knumber.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import sypztep.knumber.client.particle.behavior.*;
import sypztep.knumber.client.particle.util.Easing;

public final class TextParticle extends Particle {
    private final ParticleAnimation animation;
    private final ParticleRenderer renderer;
    private final Matrix4f renderMatrix;

    private String text;
    private float scale;
    private float maxSize;

    public TextParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.maxAge = 20;
        this.scale = 0.0F;
        this.maxSize = -0.045F;
        this.gravityStrength = -0.125f;
        this.renderMatrix = new Matrix4f();
        this.animation = new ElasticAnimation(new Easing.ElasticOut());
        this.renderer = new OutlinedTextRenderer();
    }

    @Override
    public void tick() {
        this.scale = animation.animate(this.age++, this.maxAge, 0.0F, this.maxSize);
        if (this.age > this.maxAge && this.scale >= 0) {
            this.markDead();
        }
        super.tick();
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float particleX = (float) (MathHelper.lerp(tickDelta, prevPosX, x) - cameraPos.x);
        float particleY = (float) (MathHelper.lerp(tickDelta, prevPosY, y) - cameraPos.y);
        float particleZ = (float) (MathHelper.lerp(tickDelta, prevPosZ, z) - cameraPos.z);

        setupRenderMatrix(camera, particleX, particleY, particleZ);

        MinecraftClient client = MinecraftClient.getInstance();
        int color = calculateColor();

        RenderContext context = new RenderContext(
                client.textRenderer,
                client.getBufferBuilders().getEntityVertexConsumers(),

                renderMatrix,
                text,
                client.textRenderer.getWidth(text) / -2.0F,
                0.0F,
                color,
                0xF000F0
        );

        renderer.render(context);
    }

    @Override
    public void renderCustom(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, float tickDelta) {
        super.renderCustom(matrices, vertexConsumers, camera, tickDelta);
    }

    private void setupRenderMatrix(Camera camera, float x, float y, float z) {
        renderMatrix.identity()
                .translation(x, y, z)
                .rotate(camera.getRotation())
                .rotate((float) Math.PI, 0.0F, 1.0F, 0.0F)
                .scale(scale);
    }

    private int calculateColor() {
        return ((int)(alpha * 255.0F) << 24) |
                ((int)(red * 255.0F) << 16) |
                ((int)(green * 255.0F) << 8) |
                ((int)(blue * 255.0F));
    }

    // Setters and other methods remain the same
    public void setText(String text) { this.text = text; }
    public void setMaxSize(float maxSize) { this.maxSize = maxSize; }
    public void setColor(int red, int green, int blue) {
        super.setColor(red / 255.0f, green / 255.0f, blue / 255.0f);
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red / 255.0f, green / 255.0f, blue / 255.0f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }
}