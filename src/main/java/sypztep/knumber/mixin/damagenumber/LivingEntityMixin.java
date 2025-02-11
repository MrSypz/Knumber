package sypztep.knumber.mixin.damagenumber;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
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
    @Unique
    private float previousHealth;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void healing(CallbackInfo info) {
        if (!ModConfig.damageNumberIndicator) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        if (previousHealth == 0) {
            previousHealth = entity.getHealth();
            return;
        }

        float newHealth = entity.getHealth();
        float healthDiff = newHealth - previousHealth;
        if (healthDiff < 0) {
            previousHealth = newHealth;
            String damageText = String.format("%.2f", -healthDiff);
            ParticleUtil.spawnTextParticle(entity, Text.of(ModConfig.damageIcon + damageText), new Color(ModConfig.normalDamageColor), -0.055f, -0.6f);
            return;
        }
        if (healthDiff > 0 && healthDiff != entity.getMaxHealth()) {
            previousHealth = newHealth;
            String healText = String.format("%.2f", healthDiff);
            ParticleUtil.spawnTextParticle(entity, Text.of(ModConfig.healIcon + healText), new Color(ModConfig.healingColor), -0.055f, -0.6f);
        } else {
            previousHealth = newHealth;
        }
    }
}