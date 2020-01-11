package mrunknown404.itemfav;

import java.io.File;

import mrunknown404.itemfav.handler.MasterEventHandler;
import mrunknown404.itemfav.util.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MOD_ID, useMetadata = true)
public class Main {
	public static final String MOD_ID = "itemfav";
	
	@Instance
	public static Main main;
	
	@SidedProxy(clientSide = "mrunknown404.itemfav.util.proxy.ClientProxy", serverSide = "mrunknown404.itemfav.util.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static File dir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new MasterEventHandler());
		
		proxy.setupKeybind();
		
		dir = new File(e.getModConfigurationDirectory(), "/ItemFavorites/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}
}
