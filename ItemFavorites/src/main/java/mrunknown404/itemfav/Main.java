package mrunknown404.itemfav;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import mrunknown404.itemfav.handler.MasterEventHandler;
import mrunknown404.itemfav.utils.LockHandler;
import mrunknown404.itemfav.utils.compat.InvTweaksCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MOD_ID, clientSideOnly = true, useMetadata = true, dependencies = "required-after:unknownlibs@[1.0.3,)")
public class Main {
	public static final String MOD_ID = "itemfav";
	
	public static final KeyBinding KEY_LOCK_ITEM = new KeyBinding("key.lockitem", Keyboard.KEY_R, "key.itemlock.category");
	public static final KeyBinding KEY_NEW_DROP = new KeyBinding("key.drop", Keyboard.KEY_Q, "key.categories.inventory") {
		@Override
		public boolean isActiveAndMatches(int keyCode) {
			if (mc.player == null || mc.currentScreen != null) {
				return super.isActiveAndMatches(keyCode);
			}
			
			return !LockHandler.isSlotLocked(mc.player.inventory.currentItem) && super.isActiveAndMatches(keyCode);
		}
		
		@Override
		public boolean isKeyDown() {
			if (mc.player == null || mc.currentScreen != null) {
				return super.isKeyDown();
			}
			
			return !LockHandler.isSlotLocked(mc.player.inventory.currentItem) && super.isKeyDown();
		}
	};
	
	private static Minecraft mc;
	
	@Instance
	public static Main main;
	
	public static File dir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new MasterEventHandler());
		ClientRegistry.registerKeyBinding(KEY_LOCK_ITEM);
		mc = Minecraft.getMinecraft();
		
		GameSettings s = mc.gameSettings;
		s.keyBindings = ArrayUtils.removeElement(s.keyBindings, s.keyBindDrop);
		s.keyBindings = ArrayUtils.add(s.keyBindings, KEY_NEW_DROP);
		s.keyBindDrop = KEY_NEW_DROP;
		s.loadOptions();
		
		InvTweaksCompat.dir = Paths.get(e.getModConfigurationDirectory() + "/InvTweaksRules.txt");
		
		dir = new File(e.getModConfigurationDirectory(), "/ItemFavorites/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
