package sypztep.knumber.client.particle.behavior;

import net.minecraft.util.math.MathHelper;
import sypztep.knumber.client.particle.util.Easing;

public class ElasticAnimation implements ParticleAnimation {
    private static final float ANIMATION_DURATION = 10.0F;
    private final Easing easing;

    public ElasticAnimation(Easing easing) {
        this.easing = easing;
    }

    @Override
    public float animate(float age, float maxAge, float startValue, float endValue) {
        if (age <= ANIMATION_DURATION) {
            return MathHelper.lerp(
                    easing.ease(age / ANIMATION_DURATION, 0.0F, 1.0F, 1.0F),
                    startValue,
                    endValue
            );
        }
        return MathHelper.lerp(
                Easing.CUBIC_IN.ease((age - ANIMATION_DURATION) / ANIMATION_DURATION, 0.0F, 1.0F, 1.0F),
                endValue,
                startValue
        );
    }
}
