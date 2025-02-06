package sypztep.knumber.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import sypztep.knumber.ModConfig;
import sypztep.knumber.client.payload.AddTextParticlesPayload;

public class KnumberClient implements ClientModInitializer {
    public static final String MODID = "knumber";
    public static ModConfig config = new ModConfig();

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientPlayNetworking.registerGlobalReceiver(AddTextParticlesPayload.ID, new AddTextParticlesPayload.Receiver());
    }
}
