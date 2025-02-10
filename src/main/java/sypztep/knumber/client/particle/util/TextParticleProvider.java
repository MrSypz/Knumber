package sypztep.knumber.client.particle.util;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TextParticleProvider {
    private static final Map<Integer, TextParticleProvider> REGISTRY = new HashMap<>();
    private static int nextFlag = 0;

    private final int flag;
    private final Text text;
    private final Color color;
    private final float maxSize;
    private final float yPos;
    private final Supplier<Boolean> configSupplier; // Changed to Supplier<Boolean>

    private TextParticleProvider(Text text, Color color, float maxSize, float yPos, Supplier<Boolean> configSupplier) {
        this.flag = nextFlag++;
        this.text = text;
        this.color = color;
        this.maxSize = maxSize;
        this.yPos = yPos;
        this.configSupplier = configSupplier != null ? configSupplier : () -> true;
        REGISTRY.put(this.flag, this);
    }

    public static TextParticleProvider register(Text text) {
        return register(text, new Color(255, 255, 255), -0.045f, 0f, null);
    }

    public static TextParticleProvider register(Text text, float maxSize) {
        return register(text, new Color(255, 255, 255), maxSize, 0f, null);
    }

    public static TextParticleProvider register(Text text, Color color, float maxSize) {
        return register(text, color, maxSize, 0f, null);
    }

    public static TextParticleProvider register(Text text, Color color, float maxSize, float yPos) {
        return register(text, color, maxSize, yPos, null);
    }
    /**
     * Registers a fully customizable text particle with configuration support.
     *
     * @param text           The text to display
     * @param color         The color of the particle
     * @param maxSize       The maximum size of the particle
     * @param yPos          The vertical position offset
     * @param configSupplier A supplier that determines if the particle should spawn
     * @return A new TextParticleProvider instance
     *
     * @example Usage with mod configs:
     * <pre>
     * {@code
     * // Using with your mod's config
     * TextParticleProvider damageNumber = TextParticleProvider.register(
     *     Text.translatable("mod.damage"),
     *     Color.RED,
     *     -0.045f,
     *     0f,
     *     () -> ModConfig.damageNumberIndicator
     * );
     *
     * // Using with another mod's config
     * TextParticleProvider otherModEffect = TextParticleProvider.register(
     *     Text.translatable("othermod.effect"),
     *     Color.BLUE,
     *     -0.045f,
     *     0f,
     *     () -> OtherModConfig.getInstance().isEffectEnabled()
     * );
     * }
     * </pre>
     * @since 1.0.2
     */
    public static TextParticleProvider register(Text text, Color color, float maxSize, float yPos, Supplier<Boolean> configSupplier) {
        return new TextParticleProvider(text, color, maxSize, yPos, configSupplier);
    }

    public int getFlag() {
        return flag;
    }

    /**
     * Handles the spawning of particles based on the configuration.
     *
     * @param entity The entity to spawn the particle on
     * @param flag   The unique identifier of the particle provider
     */
    public static void handleParticle(Entity entity, int flag) {
        TextParticleProvider particle = REGISTRY.get(flag);
        if (particle != null && particle.configSupplier.get()) {
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
