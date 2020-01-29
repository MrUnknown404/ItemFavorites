package mrunknown404.itemfav.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.utils.compat.InvTweaksCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Main.MOD_ID)
@Config.LangKey(Main.MOD_ID + ".config.title")
public class LockHandler {
	/** Don't get directly use {@link LockHandler#getLockedArray} */
	private static String[] lockedArray = getDefaultLockedArray();
	
	@Config.Comment("Hex color for lock")
	public static String hexLockColor = "#33c0ff";
	
	public static void saveToFile() {
		Gson g = new GsonBuilder().create();
		String name;
		if (Minecraft.getMinecraft().getCurrentServerData() != null) {
			name = Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":")[0].replace(".", "-");
		} else {
			String str = DimensionManager.getCurrentSaveRootDirectory().toString();
			name = str.substring(str.lastIndexOf('\\') + 1).replace(" ", "");
		}
		System.out.println("Saving to file: " + Main.dir + "/" + name + ".dat");
		
		try {
			FileWriter fw = new FileWriter(Main.dir + "/" + name + ".dat");
			g.toJson(lockedArray, fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (Loader.isModLoaded("inventorytweaks")) {
			InvTweaksCompat.reload();
		}
	}
	
	public static void readFromFile() {
		Gson g = new GsonBuilder().create();
		String name;
		if (Minecraft.getMinecraft().getCurrentServerData() != null) {
			name = Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":")[0].replace(".", "-");
		} else {
			String str = DimensionManager.getCurrentSaveRootDirectory().toString();
			name = str.substring(str.lastIndexOf('\\') + 1).replace(" ", "");
		}
		System.out.println("Reading from file: " + Main.dir + "/" + name + ".dat");
		
		if (!new File(Main.dir + "/" + name + ".dat").exists()) {
			System.out.println("Data does not exist. Creating now...");
			lockedArray = getDefaultLockedArray();
			saveToFile();
			return;
		}
		
		try {
			FileReader fr = new FileReader(Main.dir + "/" + name + ".dat");
			lockedArray = g.fromJson(fr, new TypeToken<String[]>() {}.getType());
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!checkIfArrayIsValid()) {
			System.err.println("Loaded invalid array!");
			lockedArray = getDefaultLockedArray();
			saveToFile();
		}
		
		if (Loader.isModLoaded("inventorytweaks")) {
			InvTweaksCompat.reload();
		}
	}
	
	public static boolean isSlotLocked(Slot slot) {
		checkEmptyAndUpdate(slot);
		return isSlotLocked(slot.getSlotIndex());
	}
	
	public static boolean isSlotLocked(int slot) {
		return getLockedArray()[slot];
	}
	
	private static void checkEmptyAndUpdate(Slot slot) {
		if (!slot.getHasStack() && getLockedArray()[slot.getSlotIndex()]) {
			lockedArray[slot.getSlotIndex()] = "false";
			saveToFile();
		}
	}
	
	public static void toggleSlot(Slot slot) {
		lockedArray[slot.getSlotIndex()] = "" + !getLockedArray()[slot.getSlotIndex()];
		saveToFile();
	}
	
	/** Use this instead of {@link LockHandler#lockedArray}
	 * @return {@link LockHandler#lockedArray} but modified
	 */
	public static boolean[] getLockedArray() {
		if (!checkIfArrayIsValid()) {
			lockedArray = getDefaultLockedArray();
		}
		
		boolean[] arr = new boolean[41];
		for (int i = 0; i < 41; i++) {
			arr[i] = Boolean.parseBoolean(lockedArray[i]);
		}
		
		return arr;
	}
	
	private static String[] getDefaultLockedArray() {
		String[] arr = new String[41];
		for (int i = 0; i < 41; i++) {
			arr[i] = "false";
		}
		
		return arr;
	}
	
	private static boolean checkIfArrayIsValid() {
		if (lockedArray == null || lockedArray.length != 41) {
			return false;
		}
		
		for (int i = 0; i < 41; i++) {
			try {
				Boolean.parseBoolean(lockedArray[i]);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		return true;
	}
	
	@EventBusSubscriber(modid = Main.MOD_ID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
			if (e.getModID().equals(Main.MOD_ID)) {
				ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
