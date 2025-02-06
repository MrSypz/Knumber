package sypztep.knumber.client.particle.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import sypztep.knumber.client.particle.TextParticle;

import java.awt.*;

public final class ParticleUtil {
    private ParticleUtil() {}

    public static TextParticleBuilder createParticle(Entity target) {
        return new TextParticleBuilder(target);
    }

    public static class TextParticleBuilder {
        private final Entity target;
        private Text text;
        private Color color = new Color(255, 255, 255);
        private float maxSize = -0.045F;
        private float yPos = 0;

        private TextParticleBuilder(Entity target) {
            this.target = target;
        }

        public TextParticleBuilder setText(Text text) {
            this.text = text;
            return this;
        }

        public TextParticleBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public TextParticleBuilder setMaxSize(float maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public TextParticleBuilder setYOffset(float yPos) {
            this.yPos = yPos;
            return this;
        }

        public void spawn() {
            if (!target.getWorld().isClient()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;
            if (world == null) return;

            Vec3d particlePos = target.getPos().add(0.0, target.getHeight() + 0.45 + yPos, 0.0);
            TextParticle particle = new TextParticle(world, particlePos.x, particlePos.y, particlePos.z);
            particle.setText(text.getString());
            particle.setColor(color.getRed(), color.getGreen(), color.getBlue());
            particle.setMaxSize(maxSize);
            client.particleManager.addParticle(particle);
        }
    }

    // Legacy methods for backward compatibility
    public static void spawnTextParticle(Entity target, Text text, Color color, float maxSize, float yPos) {
        createParticle(target)
                .setText(text)
                .setColor(color)
                .setMaxSize(maxSize)
                .setYOffset(yPos)
                .spawn();
    }

    public static void spawnTextParticle(Entity target, Text text, Color color, float maxSize) {
        spawnTextParticle(target, text, color, maxSize, 0);
    }

    public static void spawnTextParticle(Entity target, Text text, float maxSize) {
        spawnTextParticle(target, text, new Color(255, 255, 255), maxSize, 0);
    }

    public static void spawnTextParticle(Entity target, Text text) {
        spawnTextParticle(target, text, new Color(255, 255, 255), -0.045F, 0);
    }
}