package mrunknown404.itemfav.utils;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mrunknown404.itemfav.ItemFav;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.gui.GuiUtils;

public class FavoriteRenderer {
	public static final ResourceLocation LOCK_ICON = new ResourceLocation(ItemFav.MOD_ID, "textures/gui/lock.png");
	
	public static void drawScreen(PoseStack stack, AbstractContainerScreen<?> screen) {
		ConfigManager cfg = ConfigManager.INSTANCE;
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, LOCK_ICON);
		RenderSystem.setShaderColor(cfg.red.get() / 255f, cfg.green.get() / 255f, cfg.blue.get() / 255f, 1);
		
		stack.scale(0.5f, 0.5f, 1);
		
		List<Slot> slots = screen.getMenu().slots;
		for (int i = 0; i < slots.size(); i++) {
			if (slots.get(i).container instanceof Inventory) {
				Slot slot = slots.get(i);
				
				if (FavoriteH.isSlotLocked(slot)) {
					GuiUtils.drawTexturedModalRect(stack, slot.x * 2, slot.y * 2, 0, 0, 7, 9, 300f);
				}
			}
		}
		
		stack.scale(2f, 2f, 1f);
		
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
	
	public static void drawHotbar(PoseStack stack) {
		ConfigManager cfg = ConfigManager.INSTANCE;
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, LOCK_ICON);
		RenderSystem.setShaderColor(cfg.red.get() / 255f, cfg.green.get() / 255f, cfg.blue.get() / 255f, 1);
		
		stack.scale(0.5f, 0.5f, 1f);
		
		Minecraft mc = Minecraft.getInstance();
		int y = mc.getWindow().getGuiScaledHeight() - 18;
		
		for (int i = 0; i < 9; i++) {
			if (FavoriteH.isSlotLocked(i)) {
				int x = (mc.getWindow().getGuiScaledWidth() / 2 - 87) + (i * 20);
				GuiUtils.drawTexturedModalRect(stack, x * 2, y * 2, 0, 0, 7, 9, 300f);
			}
		}
		
		if (FavoriteH.isSlotLocked(Inventory.SLOT_OFFHAND)) {
			GuiUtils.drawTexturedModalRect(stack, 194, y * 2, 0, 0, 7, 9, 300f);
		}
		
		stack.scale(2f, 2f, 1f);
		
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
}
