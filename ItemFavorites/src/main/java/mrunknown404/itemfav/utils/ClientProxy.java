package mrunknown404.itemfav.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;

import com.mojang.blaze3d.platform.InputConstants;

import mrunknown404.itemfav.ItemFav;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ClientProxy {
	public static final KeyMapping KEY_FAV = new KeyMapping("itemfav.key.favorite", InputConstants.KEY_R, "key.categories.inventory");
	private static final KeyMapping KEY_NEW_DROP = new KeyMapping("key.drop", 81, "key.categories.inventory") {
		
		@Override
		public boolean isActiveAndMatches(InputConstants.Key keyCode) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null && mc.screen != null) {
				return super.isActiveAndMatches(keyCode);
			}
			
			return !FavoriteH.isSlotLocked(mc.player.getInventory().selected) && super.isActiveAndMatches(keyCode);
		}
	};
	
	public static void run(@SuppressWarnings("unused") FMLClientSetupEvent e) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.SPEC);
		ClientRegistry.registerKeyBinding(KEY_FAV);
		
		try {
			Minecraft mc = Minecraft.getInstance();
			Options s = mc.options;
			s.keyMappings = ArrayUtils.removeElement(s.keyMappings, s.keyDrop);
			Field f = ObfuscationReflectionHelper.findField(Options.class, "f_92094_");
			f.set(s, KEY_NEW_DROP);
			s.keyMappings = ArrayUtils.add(s.keyMappings, KEY_NEW_DROP);
			s.load();
		} catch (IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		
		if (!ItemFav.FAVORITE_SAVE_LOC.exists()) {
			ItemFav.FAVORITE_SAVE_LOC.mkdirs();
		}
	}
}
