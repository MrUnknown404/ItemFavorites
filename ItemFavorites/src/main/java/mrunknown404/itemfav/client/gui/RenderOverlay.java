package mrunknown404.itemfav.client.gui;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.utils.LockHandler;
import mrunknown404.itemfav.utils.ModConfig;
import mrunknown404.unknownlibs.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;

@SuppressWarnings("deprecation")
public class RenderOverlay {
	public static final ResourceLocation LOCK_ICON = new ResourceLocation(Main.MOD_ID, "textures/gui/lock.png");
	private static Minecraft mc = Minecraft.getInstance();
	
	public static void drawScreen(ContainerScreen<?> gui) {
		List<Slot> slots = gui.getContainer().inventorySlots;
		mc.getTextureManager().bindTexture(LOCK_ICON);
		
		Color c = ColorUtils.hex2Color(ModConfig.hexLockColor.get());
		
		GlStateManager.disableLighting();
		GlStateManager.color4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
		GlStateManager.translatef(0, 0, 300);
		GlStateManager.scalef(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < slots.size(); i++) {
			if (slots.get(i).inventory instanceof PlayerInventory) {
				Slot slot = slots.get(i);
				
				if (slot.getSlotIndex() < 41 && LockHandler.isSlotLocked(slot)) {
					GuiUtils.drawTexturedModalRect((slot.xPos) * 2, (slot.yPos) * 2, 0, 0, 7, 9, 0f);
				}
			}
		}
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.scalef(2, 2, 2);
		GlStateManager.translatef(0, 0, -300);
	}
	
	public static void drawHotbar() {
		mc.getTextureManager().bindTexture(LOCK_ICON);
		Color c = ColorUtils.hex2Color(ModConfig.hexLockColor.get());
		
		GlStateManager.color4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
		GlStateManager.translatef(0, 0, 300);
		GlStateManager.scalef(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < 9; i++) {
			if (!LockHandler.isSlotLocked(i)) {
				continue;
			}
			
			int x = (mc.getMainWindow().getScaledWidth() / 2 - 87) + (i * 20), y = mc.getMainWindow().getScaledHeight() - 18;
			GuiUtils.drawTexturedModalRect(x * 2, y * 2, 0, 0, 7, 9, 0f);
		}
		
		if (LockHandler.isSlotLocked(40) && !mc.player.inventory.offHandInventory.get(0).isEmpty()) {
			GuiUtils.drawTexturedModalRect(194, ( mc.getMainWindow().getScaledHeight() - 18) * 2, 0, 0, 7, 9, 0f);
		}
		
		GlStateManager.color4f(1, 1, 1, 1f);
		GlStateManager.scalef(2, 2, 2);
		GlStateManager.translatef(0, 0, -300);
	}
}
