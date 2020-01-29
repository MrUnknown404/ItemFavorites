package mrunknown404.itemfav.utils.compat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import mrunknown404.itemfav.utils.LockHandler;
import mrunknown404.unknownlibs.utils.MathUtils;

public class InvTweaksCompat {
	public static final String START_LINE = "# ItemFavorites Config (Warning! Everything past this line will be deleted!) ->";
	public static Path dir;
	
	public static void reload() {
		List<String> lockLines = new ArrayList<String>();
		for (int i = 0; i < 36; i++) {
			boolean has = LockHandler.isSlotLocked(i);
			
			String prefix = "null";
			if (has) {
				if (MathUtils.within(i, 0, 8)) {
					prefix = "D";
				} else if (MathUtils.within(i, 9, 17)) {
					prefix = "A";
				} else if (MathUtils.within(i, 18, 26)) {
					prefix = "B";
				} else if (MathUtils.within(i, 27, 35)) {
					prefix = "C";
				}
				
				lockLines.add(prefix + ((i % 9) + 1) + " LOCKED");
			}
		}
		
		try {
			List<String> lines = Files.readAllLines(dir, StandardCharsets.UTF_8);
			if (lines == null || lines.isEmpty()) {
				System.err.println("COULD NOT FIND 'InvTweaksRules.txt'!");
				return;
			}
			
			if (!lines.contains(START_LINE)) {
				lines.add("");
			} else {
				lines = lines.subList(0, lines.indexOf(START_LINE));
			}
			
			lines.add(START_LINE);
			for (String s : lockLines) {
				lines.add(s);
			}
			
			Files.write(dir, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
