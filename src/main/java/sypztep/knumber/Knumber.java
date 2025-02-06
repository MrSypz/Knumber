package sypztep.knumber;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import sypztep.knumber.client.init.ModParticles;
import sypztep.knumber.client.payload.AddTextParticlesPayload;

public class Knumber implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(AddTextParticlesPayload.ID, AddTextParticlesPayload.CODEC); // Server to Client

        ModParticles.init();
    }
}
