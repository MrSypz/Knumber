package sypztep.knumber.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.knumber.ModConfig;
import sypztep.knumber.client.particle.util.ParticleUtil;

import java.awt.*;

public record DamageNumberPayload(int entityId, float amount) implements CustomPayload {
    public static final Id<DamageNumberPayload> ID = CustomPayload.id("damage_number");
    public static final PacketCodec<PacketByteBuf, DamageNumberPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            DamageNumberPayload::entityId,
            PacketCodecs.FLOAT,
            DamageNumberPayload::amount,
            DamageNumberPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId, float amount) {
        ServerPlayNetworking.send(player, new DamageNumberPayload(entityId, amount));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<DamageNumberPayload> {
        @Override
        public void receive(DamageNumberPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            float amount = payload.amount();

            if (entity != null && ModConfig.damageNumberIndicator) {
                if (amount > 0) {
                    String damageText = String.format("%.2f", amount);
                    ParticleUtil.spawnTextParticle(entity, Text.of(damageText),
                            new Color(ModConfig.normalDamageColor), -0.055f, -0.6f);
                }
            }
        }
    }
}
