package sypztep.knumber;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import sypztep.knumber.client.KnumberClient;

@Config(name = KnumberClient.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("feature-client")
    public static boolean missingIndicator = true;
    @ConfigEntry.Category("feature-client")
    public static boolean damageIndicator = true;
    @ConfigEntry.Category("feature-client")
    public static boolean damageNumberIndicator = true;

    @ConfigEntry.Category("feature-client")
    public static boolean flickParticle = true;

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    public static int normalDamageColor = 0xD43333;

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    public static int magicDamageColor = 0x3A57D6; // Blue color code

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    public static int trueDamageColor = 0x8A2BE2; // Purple color code

}
