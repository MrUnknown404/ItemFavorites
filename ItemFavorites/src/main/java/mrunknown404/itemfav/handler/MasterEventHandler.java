package mrunknown404.itemfav.handler;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.client.gui.RenderOverlay;
import mrunknown404.itemfav.utils.LockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MasterEventHandler {
	@SubscribeEvent
	public void onConnect(ClientPlayerNetworkEvent.LoggedInEvent e) {
		if (e.getPlayer() == Minecraft.getInstance().player) {
			LockHandler.readFromFile();
		}
	}
	
	@SubscribeEvent
	public void onGuiKeyboardInputEventPress(GuiScreenEvent.KeyboardKeyEvent e) {
		if (Minecraft.getInstance().gameSettings.keyBindDrop.getKey().getKeyCode() == e.getKeyCode() && e.getGui() instanceof ContainerScreen<?>) { //Doesn't trigger?
			Slot slot = ((ContainerScreen<?>) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof PlayerInventory) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
					return;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiKeyboardInputEventRelease(KeyboardKeyReleasedEvent.Post e) {
		if (Main.KEY_LOCK_ITEM.isKeyDown() && e.getGui() instanceof ContainerScreen<?>) {
			Slot slot = ((ContainerScreen<?>) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof PlayerInventory) {
				if (slot.getHasStack()) {
					LockHandler.toggleSlot(slot);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiScreenEvent(MouseInputEvent e) {
		if (e.getGui() instanceof ContainerScreen<?>) {
			Slot slot = ((ContainerScreen<?>) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof PlayerInventory) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderOverlayEvent(RenderGameOverlayEvent.Post e) {
		if (e.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE && e.getType() != RenderGameOverlayEvent.ElementType.JUMPBAR) {
			return;
		}
		if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && !Minecraft.getInstance().player.isSpectator()) {
			RenderOverlay.drawHotbar();
		}
	}
	
	@SubscribeEvent
	public void onContainerForegroundEvent(GuiContainerEvent.DrawForeground e) {
		ContainerScreen<?> gui = e.getGuiContainer();
		if (gui == null) {
			return;
		}
		
		RenderOverlay.drawScreen(gui);
	}
}
