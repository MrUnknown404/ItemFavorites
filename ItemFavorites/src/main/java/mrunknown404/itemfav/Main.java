package mrunknown404.itemfav;

import java.io.File;

import org.lwjgl.input.Keyboard;

import mrunknown404.itemfav.handler.MasterEventHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MOD_ID, clientSideOnly = true, useMetadata = true, dependencies = "required-after:unknownlibs@1.0.1")
public class Main {
	public static final String MOD_ID = "itemfav";
	
	public static final KeyBinding KEY_LOCK_ITEM = new KeyBinding("key.lockitem", Keyboard.KEY_R, "key.itemlock.category");
	
	@Instance
	public static Main main;
	
	public static File dir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new MasterEventHandler());
		ClientRegistry.registerKeyBinding(KEY_LOCK_ITEM);
		
		dir = new File(e.getModConfigurationDirectory(), "/ItemFavorites/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
