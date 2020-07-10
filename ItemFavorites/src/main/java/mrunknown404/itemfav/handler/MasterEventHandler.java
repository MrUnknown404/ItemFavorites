package mrunknown404.itemfav.handler;

import org.lwjgl.glfw.GLFW;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import mrunknown404.itemfav.Main;
import mrunknown404.itemfav.client.gui.RenderOverlay;
import mrunknown404.itemfav.utils.LockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MasterEventHandler {
	@SubscribeEvent
	public void onConnect(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof PlayerEntity && e.getEntity() == Minecraft.getInstance().player) {
			LockHandler.readFromFile();
		}
	}
	
	@SubscribeEvent
	public void onGuiKeyboardInputEvent(KeyboardKeyReleasedEvent e) {
		if (GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(),
				Minecraft.getInstance().gameSettings.keyBindDrop.getKey().getKeyCode()) == GLFW.GLFW_RELEASE && e.getGui() instanceof ContainerScreen<?>) {
			Slot slot = ((ContainerScreen<?>) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof PlayerInventory) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
					return;
				}
			}
		}
		
		if (GLFW.glfwGetKey(Minecraft.getInstance().getMainWindow().getHandle(), Main.KEY_LOCK_ITEM.getKey().getKeyCode()) == GLFW.GLFW_RELEASE &&
				e.getGui() instanceof ContainerScreen<?>) {
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
	
	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent e) {
		if (LockHandler.isSlotLocked(Minecraft.getInstance().player.inventory.currentItem)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onNetworkEstablished(ClientPlayerNetworkEvent.LoggedInEvent e) {
		e.getNetworkManager().channel().pipeline().addLast(new ChannelHandler[] { new DropFetcher() });
	}
	
	private static class DropFetcher extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			boolean blocked = false;
			try {
				if (!(msg instanceof CPlayerDiggingPacket)) {
					return;
				}
				
				CPlayerDiggingPacket.Action action = ((CPlayerDiggingPacket) msg).getAction();
				if (action != CPlayerDiggingPacket.Action.DROP_ITEM && action != CPlayerDiggingPacket.Action.DROP_ALL_ITEMS) {
					return;
				}
				
				if (!LockHandler.isSlotLocked(Minecraft.getInstance().player.inventory.currentItem)) {
					return;
				}
				blocked = true;
			} finally {
				if (!blocked) {
					super.write(ctx, msg, promise);
				}
			}
		}
	}
}
