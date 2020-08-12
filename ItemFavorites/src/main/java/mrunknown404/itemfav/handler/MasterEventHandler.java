package mrunknown404.itemfav.handler;

import org.lwjgl.input.Mouse;

import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.client.gui.RenderOverlay;
import mrunknown404.itemfav.utils.LockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MasterEventHandler {
	@SubscribeEvent
	public void onConnect(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer && e.getEntity() == Minecraft.getMinecraft().player) {
			LockHandler.readFromFile();
		}
	}
	
	@SubscribeEvent
	public void onGuiKeyboardInputEvent(KeyboardInputEvent.Pre e) {
		if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindDrop) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
					return;
				}
			}
		}
		
		if (GameSettings.isKeyDown(Main.KEY_LOCK_ITEM) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
				if (slot.getHasStack()) {
					LockHandler.toggleSlot(slot);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiScreenEvent(MouseInputEvent e) {
		if ((Mouse.getEventButton() == 0 || Mouse.getEventButton() == 1) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
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
		if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isSpectator()) {
			RenderOverlay.drawHotbar();
		}
	}
	
	@SubscribeEvent
	public void onContainerForegroundEvent(GuiContainerEvent.DrawForeground e) {
		GuiContainer gui = e.getGuiContainer();
		if (gui == null) {
			return;
		}
		
		RenderOverlay.drawScreen(gui);
	}
}
