package mrunknown404.itemfav.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mrunknown404.itemfav.ItemFav;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.storage.LevelResource;

public class FavoriteH {
	private static final Map<Integer, Boolean> FAV_MAP = new HashMap<Integer, Boolean>();
	
	public static void saveToFile() {
		try {
			Gson g = new GsonBuilder().create();
			String name = getFileName();
			FileWriter fw = new FileWriter(ItemFav.FAVORITE_SAVE_LOC + "/" + name + ".dat");
			
			Set<Integer> set = new HashSet<Integer>();
			for (Entry<Integer, Boolean> pair : FAV_MAP.entrySet()) {
				if (pair.getValue()) {
					set.add(pair.getKey());
				}
			}
			
			g.toJson(set, fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readFromFile() {
		String name = getFileName();
		
		FAV_MAP.clear();
		if (!new File(ItemFav.FAVORITE_SAVE_LOC + "/" + name + ".dat").exists()) {
			return;
		}
		
		try {
			Gson g = new GsonBuilder().create();
			FileReader fr = new FileReader(ItemFav.FAVORITE_SAVE_LOC + "/" + name + ".dat");
			
			Set<Integer> set = g.fromJson(fr, new TypeToken<Set<Integer>>() {}.getType());
			fr.close();
			
			for (int i : set) {
				FAV_MAP.put(i, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFileName() {
		Minecraft mc = Minecraft.getInstance();
		String name;
		if (mc.getCurrentServer() != null) {
			String[] temp = mc.getCurrentServer().ip.split(":");
			name = temp[0].replace(".", "-");
			if (temp.length > 1) {
				name += "_" + temp[1];
			}
		} else {
			String str = mc.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toString(); // Look for better way
			str = str.substring(0, str.length() - 2);
			name = str.substring(str.lastIndexOf('\\') + 1);
		}
		
		return name;
	}
	
	public static boolean isSlotLocked(Slot slot) {
		return FAV_MAP.getOrDefault(slot.getSlotIndex(), false);
	}
	
	public static boolean isSlotLocked(int slot) {
		return FAV_MAP.getOrDefault(slot, false);
	}
	
	public static void toggleSlot(Slot slot) {
		FAV_MAP.put(slot.getSlotIndex(), !FAV_MAP.getOrDefault(slot.getSlotIndex(), false));
		saveToFile();
	}
}
