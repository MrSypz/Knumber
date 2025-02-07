package sypztep.knumber;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import sypztep.knumber.client.KnumberClient;

@Config(name = KnumberClient.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("feature-client")
    @Comment("Damage Indicator Feature (default : true) if you want to turn it off I think delete mod will be more better :)")
    public static boolean damageNumberIndicator = true;

    @ConfigEntry.Category("feature-client")
    @Comment("When particle appear it'll have white color (default : true) can be turn off if you have a motion sick")
    public static boolean flickParticle = true;

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    @Comment("For normal damage color (default : 0xD43333)")
    public static int normalDamageColor = 0xD43333;

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    @Comment("Not implement yet")
    public static int magicDamageColor = 0x3A57D6; // Blue color code

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    @Comment("Not implement yet")
    public static int trueDamageColor = 0x8A2BE2; // Purple color code

}
