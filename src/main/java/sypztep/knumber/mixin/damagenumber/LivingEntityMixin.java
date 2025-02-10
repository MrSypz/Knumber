package sypztep.knumber.mixin.damagenumber;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.knumber.ModConfig;
import sypztep.knumber.client.particle.util.ParticleUtil;
import sypztep.knumber.client.payload.DamageNumberPayload;

import java.awt.*;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public abstract class LivingEntityMixin {
    @Unique
    private final Color HEALTH_COLOR = new Color(40, 255, 40);
    @Unique
    private float previousHealth;

    @Inject(method = "applyDamage", at = @At("RETURN"))
    private void sendDamageNumber(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.getWorld().isClient()) return;


        for (ServerPlayerEntity player : world.getPlayers())
            if (player.canSee(entity)) DamageNumberPayload.send(player, entity.getId(), amount);
             else return;
    }


    @Inject(method = "tick()V", at = @At("TAIL"))
    private void healing(CallbackInfo info) {
        if (!ModConfig.damageNumberIndicator) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        var world = entity.getWorld();
        if (world == null || !world.isClient()) return;

        if (previousHealth == 0) {
            previousHealth = entity.getHealth();
            return;
        }

        float newHealth = entity.getHealth();
        float healthDiff = newHealth - previousHealth;

        if (healthDiff > 0 && healthDiff != entity.getMaxHealth()) {
            previousHealth = newHealth;
            String healText = String.format("%.2f", healthDiff);
            ParticleUtil.spawnTextParticle(entity, Text.of("❤ " + healText), HEALTH_COLOR, -0.055f, -0.6f);
        } else {
            previousHealth = newHealth;
        }
    }
}