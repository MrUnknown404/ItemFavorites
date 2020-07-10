package mrunknown404.itemfav;

import java.io.File;
import java.io.IOException;

import mrunknown404.itemfav.handler.MasterEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "itemfav";
	
	public static final KeyBinding KEY_LOCK_ITEM = new KeyBinding("key.lockitem", 82, "key.itemlock.category");
	
	public static File dir;
	
	public Main() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
	}
	
	private void doClientStuff(@SuppressWarnings("unused") FMLClientSetupEvent e) {
		MinecraftForge.EVENT_BUS.register(new MasterEventHandler());
		ClientRegistry.registerKeyBinding(KEY_LOCK_ITEM);
		
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
