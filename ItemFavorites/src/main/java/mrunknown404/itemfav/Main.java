package mrunknown404.itemfav;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;

import mrunknown404.itemfav.handler.MasterEventHandler;
import mrunknown404.itemfav.utils.LockHandler;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "itemfav";
	
	public static final KeyBinding KEY_LOCK_ITEM = new KeyBinding("key.lockitem", 82, "key.itemlock.category");
	private static final KeyBinding KEY_NEW_DROP = new KeyBinding("key.drop", 81, "key.categories.inventory") {
		@Override
		public boolean isActiveAndMatches(InputMappings.Input keyCode) {
			if (Minecraft.getInstance().player == null && Minecraft.getInstance().currentScreen != null) {
				return super.isActiveAndMatches(keyCode);
			}
			
			return !LockHandler.isSlotLocked(Minecraft.getInstance().player.inventory.currentItem) && super.isActiveAndMatches(keyCode);
		}
	};
	
	public static File dir;
	
	public Main() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
	}
	
	private void doClientStuff(@SuppressWarnings("unused") FMLClientSetupEvent e) {
		MinecraftForge.EVENT_BUS.register(new MasterEventHandler());
		ClientRegistry.registerKeyBinding(KEY_LOCK_ITEM);
		
		try {
			GameSettings s = Minecraft.getInstance().gameSettings;
			s.keyBindings = ArrayUtils.removeElement(s.keyBindings, s.keyBindDrop);
			Field f = ObfuscationReflectionHelper.findField(GameSettings.class, "field_74316_C");
			f.set(s, KEY_NEW_DROP);
			s.keyBindings = ArrayUtils.add(s.keyBindings, KEY_NEW_DROP);
		s.loadOptions();
		} catch (IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		
		try {
			dir = new File(Minecraft.getInstance().gameDir.getCanonicalPath() + "/config/", "/ItemFavorites/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
