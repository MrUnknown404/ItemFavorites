package mrunknown404.itemfav.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import mrunknown404.itemfav.ItemFav;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ConfigManager {
	public static final ConfigManager INSTANCE;
	public static final ForgeConfigSpec SPEC;
	
	private static final Path CONFIG_PATH = Paths.get("config", ItemFav.MOD_ID + ".toml");
	
	static {
		Pair<ConfigManager, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigManager::new);
		INSTANCE = specPair.getLeft();
		SPEC = specPair.getRight();
		CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH).sync().autoreload().writingMode(WritingMode.REPLACE).build();
		config.load();
		config.save();
		SPEC.setConfig(config);
	}
	
	final ConfigValue<Integer> red, green, blue;
	
	private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
		red = configSpecBuilder.translation(ItemFav.MOD_ID + "config.red").comment("Red value").defineInRange("red", 51, 0, 255);
		green = configSpecBuilder.translation(ItemFav.MOD_ID + "config.green").comment("Green value").defineInRange("green", 192, 0, 255);
		blue = configSpecBuilder.translation(ItemFav.MOD_ID + "config.blue").comment("Blue value").defineInRange("blue", 255, 0, 255);
	}
}
