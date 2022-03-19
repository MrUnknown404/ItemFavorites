package mrunknown404.itemfav;

import java.io.File;
import java.nio.file.Paths;

import mrunknown404.itemfav.utils.ClientProxy;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(ItemFav.MOD_ID)
public class ItemFav {
	public static final String MOD_ID = "itemfav";
	
	public static final File FAVORITE_SAVE_LOC = Paths.get("config", MOD_ID).toFile();
	
	public ItemFav() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::run);
	}
}
