package sypztep.knumber.client.particle.util;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import sypztep.knumber.ModConfig;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TextParticleProvider {
    private static final Map<Integer, TextParticleProvider> REGISTRY = new HashMap<>();
    private static int nextFlag = 0;

    private final int flag;
    private final Text text;
    private final Color color;
    private final float maxSize;
    private final float yPos;
    private final boolean configDependent;
    private final String configKey;

    private TextParticleProvider(Text text, Color color, float maxSize, float yPos, boolean configDependent, String configKey) {
        this.flag = nextFlag++;
        this.text = text;
        this.color = color;
        this.maxSize = maxSize;
        this.yPos = yPos;
        this.configDependent = configDependent;
        this.configKey = configKey;
        REGISTRY.put(this.flag, this);
    }

    public static TextParticleProvider register(Text text) {
        return register(text, new Color(255, 255, 255), -0.045f, 0f, false, null);
    }

    public static TextParticleProvider register(Text text, float maxSize) {
        return register(text, new Color(255, 255, 255), maxSize, 0f, false, null);
    }

    public static TextParticleProvider register(Text text, Color color, float maxSize) {
        return register(text, color, maxSize, 0f, false, null);
    }

    public static TextParticleProvider register(Text text, Color color, float maxSize, float yPos) {
        return register(text, color, maxSize, yPos, false, null);
    }

    public static TextParticleProvider register(Text text, Color color, float maxSize, float yPos, boolean configDependent, String configKey) {
        return new TextParticleProvider(text, color, maxSize, yPos, configDependent, configKey);
    }

    public int getFlag() {
        return flag;
    }
    /**
     * Predefined TextParticleProviders used for various combat text indicators.
     *
     * <p>These constants register different types of text particles for visual feedback.</p>
     *
     * <p>Example usage:</p>
     * <pre>
     * {@code
     * public static final TextParticleProvider MISSING = TextParticleProvider.register(
     *         Text.translatable("penomior.text.missing")
     * );
     *
     * public static final TextParticleProvider BACKATTACK = TextParticleProvider.register(
     *         Text.translatable("penomior.text.back"),
     *         Color.WHITE,
     *         -0.045f,
     *         0.15f,
     *         true,
     *         "damageIndicator"
     * );
     *
     * public static final TextParticleProvider CRITICAL = TextParticleProvider.register(
     *         Text.translatable("penomior.text.critical"),
     *         new Color(1.0f, 0.310f, 0.0f),
     *         -0.055f,
     *         -0.275f,
     *         true,
     *         "damageIndicator"
     * );
     * }
     * </pre>
     */
    public static void handleParticle(Entity entity, int flag) throws NoSuchFieldException, IllegalAccessException {
        TextParticleProvider particle = REGISTRY.get(flag);
        if (particle != null) {
            boolean shouldSpawn = !particle.configDependent ||
                    (particle.configKey != null && ModConfig.class.getField(particle.configKey).getBoolean(null));

            if (shouldSpawn) {
                ParticleUtil.spawnTextParticle(
                        entity,
                        particle.text,
                        particle.color,
                        particle.maxSize,
                        particle.yPos
                );
            }
        }
    }
}
