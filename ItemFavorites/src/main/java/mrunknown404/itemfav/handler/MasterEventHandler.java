package mrunknown404.itemfav.handler;

import org.lwjgl.input.Mouse;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import mrunknown404.itemfav.client.gui.RenderOverlay;
import mrunknown404.itemfav.util.LockHandler;
import mrunknown404.itemfav.util.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterEventHandler {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onConnect(PlayerEvent.PlayerLoggedInEvent e) {
		LockHandler.readFromFile();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiKeyboardInputEvent(GuiScreenEvent.KeyboardInputEvent.Pre e) {
		if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindDrop) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
					return;
				}
			}
		}
		
		if (GameSettings.isKeyDown(ClientProxy.KEY_LOCK_ITEM) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
				if (slot.getHasStack()) {
					LockHandler.toggleSlot(slot);
				}
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiScreenEvent(GuiScreenEvent.MouseInputEvent e) {
		if (Mouse.getEventButtonState() && (Mouse.getEventButton() == 0 || Mouse.getEventButton() == 1) && e.getGui() instanceof GuiContainer) {
			Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
			
			if (slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
				if (LockHandler.isSlotLocked(slot)) {
					e.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderOverlayEvent(RenderGameOverlayEvent.Post e) {
		if ((e.isCancelable()) || ((e.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) && (e.getType() != RenderGameOverlayEvent.ElementType.JUMPBAR))) {
			return;
		}
		if ((Minecraft.getMinecraft().world != null) && (Minecraft.getMinecraft().player != null)) {
			RenderOverlay.drawHotbar();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onContainerForegroundEvent(GuiContainerEvent.DrawForeground e) {
		GuiContainer gui = e.getGuiContainer();
		if (gui == null) {
			return;
		}
		
		RenderOverlay.drawScreen(gui);
	}
	
	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent e) {
		if (LockHandler.isHotbarSlotLocked(Minecraft.getMinecraft().player.inventory.currentItem)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onNetworkEstablished(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		NetworkManager networkManager = event.getManager();
		ChannelPipeline pipeline = networkManager.channel().pipeline();
		pipeline.addLast(new ChannelHandler[] { new DropFetcher() });
	}
	
	private static class DropFetcher extends ChannelOutboundHandlerAdapter {
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			boolean blocked = false;
			try {
				if (!(msg instanceof CPacketPlayerDigging)) {
					return;
				}
				
				CPacketPlayerDigging.Action action = ((CPacketPlayerDigging) msg).getAction();
				if (action != CPacketPlayerDigging.Action.DROP_ITEM && action != CPacketPlayerDigging.Action.DROP_ALL_ITEMS) {
					return;
				}
				
				if (!LockHandler.isHotbarSlotLocked(Minecraft.getMinecraft().player.inventory.currentItem)) {
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
