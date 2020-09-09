package mrunknown404.itemfav.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModConfig {
	private static final String CATEGORY_GENERAL = "general";
	
	public static ForgeConfigSpec CLIENT_CONFIG;
	public static ConfigValue<String> hexLockColor;
	
	static {
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
		
		CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
		hexLockColor = CLIENT_BUILDER.comment("Hex Color").define("hexLockColor", "#33c0ff");
		CLIENT_BUILDER.pop();
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
}
