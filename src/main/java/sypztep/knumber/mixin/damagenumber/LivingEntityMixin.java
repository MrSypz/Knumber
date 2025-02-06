package sypztep.knumber.mixin.damagenumber;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
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

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void damageNumberIndicator(CallbackInfo info) {
        if (!ModConfig.damageNumberIndicator) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        var world = entity.getWorld();
        if (world == null || !world.isClient()) return;

        if (previousHealth == 0) {
            previousHealth = entity.getHealth();
        }

        float oldHealth = previousHealth;
        float newHealth = entity.getHealth();
        float damage = oldHealth - newHealth;

        DamageSource source = this.getRecentDamageSource();

        assert source != null;
        if (oldHealth != newHealth && Math.abs(damage) != entity.getMaxHealth() && damage >= 0 && source != null) {
            previousHealth = newHealth;
            String health = String.format("%.2f", damage);
            ParticleUtil.spawnTextParticle(entity, Text.of(health), new Color(ModConfig.normalDamageColor), -0.055f, -0.6f);
        } else if (oldHealth != newHealth && Math.abs(damage) != entity.getMaxHealth() && damage < 0) {
            previousHealth = newHealth;
            String health = String.format("%.2f", damage * -1);
            ParticleUtil.spawnTextParticle(entity, Text.of("+ " + health), new Color(40, 255, 40), -0.055f, -0.6f);
        }
    }
}