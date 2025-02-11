package sypztep.knumber;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import sypztep.knumber.client.KnumberClient;

@Config(name = KnumberClient.MODID)
public final class ModConfig implements ConfigData {
    @ConfigEntry.Category("feature-client")
    @Comment("Damage Indicator Feature (default : true) if you want to turn it off I think delete mod will be more better :)")
    public static boolean damageNumberIndicator = true;

    @ConfigEntry.Category("feature-client")
    @Comment("When particle appear it'll have white color (default : true) can be turn off if you have a motion sick")
    public static boolean flickParticle = true;

    @ConfigEntry.Category("feature-client")
    @Comment("For the icon when player healing (default : ❤ ) disable by empty this field")
    public static String healIcon = "❤ ";

    @ConfigEntry.Category("feature-client")
    @Comment("For the icon when player taking damage (default : ✖ ) disable by empty this field")
    public static String damageIcon = "✖ ";

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    @Comment("For normal damage color (default : 0xD43333)")
    public static int normalDamageColor = 0xD43333;

    @ConfigEntry.Category("feature-client")
    @ConfigEntry.ColorPicker()
    @Comment("For healing color (default : 0x4DBD44)")
    public static int healingColor = 0x4DBD44;
}
