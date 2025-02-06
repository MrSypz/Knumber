package sypztep.knumber.mixin.damagenumber;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.knumber.ModConfig;
import sypztep.knumber.client.particle.util.ParticleUtil;

import java.awt.*;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public abstract class LivingEntityMixin {
    @Shadow public abstract @Nullable DamageSource getRecentDamageSource();

    @Unique private float previousHealth;
    @Unique private static final float HEALTH_THRESHOLD = 0.01f;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void damageNumberIndicator(CallbackInfo info) {
        if (!ModConfig.damageNumberIndicator) return;
        LivingEntity entity = (LivingEntity) (Object) this;
        var world = entity.getWorld();
        if (world == null || !world.isClient()) return;

        if (previousHealth == 0) {
            previousHealth = entity.getHealth();
            return;
        }


        float newHealth = entity.getHealth();
        float damage = previousHealth - newHealth;

        if (Math.abs(damage) < HEALTH_THRESHOLD) return;

        DamageSource source = this.getRecentDamageSource();
        if (source == null) return;

        if (damage > 0 && damage != entity.getMaxHealth()) {
            displayDamageNumber(entity, damage, source);
        } else if (damage < 0 && Math.abs(damage) != entity.getMaxHealth()) {
            displayHealNumber(entity, Math.abs(damage));
        }

        previousHealth = newHealth;
    }

    @Unique
    private void displayDamageNumber(LivingEntity entity, float damage, DamageSource source) {
        ParticleUtil.createParticle(entity)
                .setText(Text.of(String.format("%.2f", damage)))
                .setColor(new Color(ModConfig.normalDamageColor))
                .setMaxSize(-0.055f)
                .setYOffset(-0.6f)
                .spawn();
    }

    @Unique
    private void displayHealNumber(LivingEntity entity, float healing) {
        ParticleUtil.createParticle(entity)
                .setText(Text.of("+ " + String.format("%.2f", healing)))
                .setColor(new Color(0, 255, 0))
                .setMaxSize(-0.055f)
                .setYOffset(-0.6f)
                .spawn();
    }
}