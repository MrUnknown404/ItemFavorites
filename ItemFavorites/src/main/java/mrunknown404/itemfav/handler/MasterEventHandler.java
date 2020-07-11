package mrunknown404.itemfav.handler;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.client.gui.RenderOverlay;
import mrunknown404.itemfav.utils.LockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MasterEventHandler {
	@SubscribeEvent
	public void onConnect(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof PlayerEntity && e.getEntity() == Minecraft.getInstance().player) {
			LockHandler.readFromFile();
		}
	}
	
	@SubscribeEvent
	public void onGuiKeyboardInputEvent(KeyboardKeyReleasedEvent.Post e) {
		/*if (Minecraft.getInstance().gameSettings.keyBindDrop.isKeyDown() && e.getGui() instanceof ContainerScreen<?>) { //Doesn't trigger?
			Slot slot = ((ContainerScreen<?>) e.getGui()).getSlotUnderMouse();
			
			System.out.println("1");
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof PlayerInventory) {
				System.out.println("2");
				if (LockHandler.isSlotLocked(slot)) {
					System.out.println("3");
					e.setCanceled(true);
					return;
				}
			}
		}*/
		
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
