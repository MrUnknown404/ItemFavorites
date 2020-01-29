package mrunknown404.itemfav.client.gui;

import java.util.List;

import org.lwjgl.util.Color;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.utils.LockHandler;
import mrunknown404.unknownlibs.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class RenderOverlay {
	public static final ResourceLocation LOCK_ICON = new ResourceLocation(Main.MOD_ID, "textures/gui/lock.png");
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void drawScreen(GuiContainer gui) {
		List<Slot> slots = gui.inventorySlots.inventorySlots;
		mc.getTextureManager().bindTexture(LOCK_ICON);
		
		Color c = ColorUtils.hex2Color(LockHandler.hexLockColor);
		
		GlStateManager.disableLighting();
		GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
		GlStateManager.translate(0, 0, 300);
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < slots.size(); i++) {
			if (slots.get(i).inventory instanceof InventoryPlayer) {
				Slot slot = slots.get(i);
				
				if (slot.getSlotIndex() < 41 && LockHandler.isSlotLocked(slot)) {
					gui.drawTexturedModalRect((slot.xPos) * 2, (slot.yPos) * 2, 0, 0, 7, 9); 
				}
			}
		}
		GlStateManager.color(1, 1, 1);
		GlStateManager.scale(2, 2, 2);
		GlStateManager.translate(0, 0, -300);
	}
	
	public static void drawHotbar() {
		ScaledResolution res = new ScaledResolution(mc);
		mc.getTextureManager().bindTexture(LOCK_ICON);
		Color c = ColorUtils.hex2Color(LockHandler.hexLockColor);
		
		GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
		GlStateManager.translate(0, 0, 300);
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < 9; i++) {
			if (!LockHandler.isSlotLocked(i)) {
				continue;
			}
			
			int x = (res.getScaledWidth() / 2 - 87) + (i * 20), y = res.getScaledHeight() - 18;
			mc.ingameGUI.drawTexturedModalRect(x * 2f, y * 2f, 0, 0, 7, 9);
		}
		
		if (LockHandler.isSlotLocked(40) && !Minecraft.getMinecraft().player.inventory.offHandInventory.get(0).isEmpty()) {
			mc.ingameGUI.drawTexturedModalRect(194, (res.getScaledHeight() - 18) * 2, 0, 0, 7, 9);
		}
		
		GlStateManager.color(1, 1, 1);
		GlStateManager.scale(2, 2, 2);
		GlStateManager.translate(0, 0, -300);
	}
}
