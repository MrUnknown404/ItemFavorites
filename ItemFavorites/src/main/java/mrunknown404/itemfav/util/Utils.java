package mrunknown404.itemfav.util;

import org.lwjgl.util.Color;

public class Utils {
	public static boolean isValidHexColor(String hexColor) {
		if (!hexColor.startsWith("#")) {
			return false;
		}
		
		hexColor = hexColor.substring(1);
		
		if (hexColor.length() == 0 || (hexColor.charAt(0) != '-' && Character.digit(hexColor.charAt(0), 16) == -1)) {
			return false;
		}
		if (hexColor.length() == 1 && hexColor.charAt(0) == '-') {
			return false;
		}
		
		for (int i = 1; i < hexColor.length(); i++) {
			if (Character.digit(hexColor.charAt(i), 16) == -1) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}
}
