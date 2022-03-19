package mrunknown404.itemfav.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void onConnect(ClientPlayerNetworkEvent.LoggedInEvent e) {
		Minecraft mc = Minecraft.getInstance();
		if (e.getPlayer() == mc.player) {
			FavoriteH.readFromFile();
		}
	}
	
	@SubscribeEvent
	public void onKeyPress(ScreenEvent.KeyboardKeyPressedEvent.Pre e) {
		Screen screen = e.getScreen();
		
		if (screen instanceof AbstractContainerScreen) {
			AbstractContainerScreen<?> cs = (AbstractContainerScreen<?>) screen;
			Slot slot = cs.getSlotUnderMouse();
			
			if (slot != null && slot.container instanceof Inventory) {
				Minecraft mc = Minecraft.getInstance();
				if (e.getKeyCode() == ClientProxy.KEY_FAV.getKey().getValue()) {
					FavoriteH.toggleSlot(slot);
				} else if (e.getKeyCode() == mc.options.keyDrop.getKey().getValue()) {
					e.setCanceled(FavoriteH.isSlotLocked(slot));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMousePress(ScreenEvent.MouseClickedEvent.Pre e) {
		Screen screen = e.getScreen();
		if (screen instanceof AbstractContainerScreen) {
			e.setCanceled(checkMouse((AbstractContainerScreen<?>) screen, e.getButton()));
		}
	}
	
	@SubscribeEvent
	public void onMouseRelease(ScreenEvent.MouseReleasedEvent.Pre e) {
		Screen screen = e.getScreen();
		if (screen instanceof AbstractContainerScreen) {
			e.setCanceled(checkMouse((AbstractContainerScreen<?>) screen, e.getButton()));
		}
	}
	
	private static boolean checkMouse(AbstractContainerScreen<?> screen, int button) {
		Slot slot = screen.getSlotUnderMouse();
		
		if (slot != null && slot.container instanceof Inventory) {
			Minecraft mc = Minecraft.getInstance();
			if (button == mc.options.keyAttack.getKey().getValue() || button == mc.options.keyUse.getKey().getValue()) {
				return FavoriteH.isSlotLocked(slot);
			}
		}
		
		return false;
	}
	
	@SubscribeEvent
	public void onScreenDraw(ContainerScreenEvent.DrawForeground e) {
		FavoriteRenderer.drawScreen(e.getPoseStack(), e.getContainerScreen());
	}
	
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.ALL) {
			FavoriteRenderer.drawHotbar(e.getMatrixStack());
		}
	}
}
