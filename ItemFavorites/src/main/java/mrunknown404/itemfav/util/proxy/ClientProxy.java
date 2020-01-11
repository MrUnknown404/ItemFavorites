package mrunknown404.itemfav.util.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	public static final KeyBinding KEY_LOCK_ITEM = new KeyBinding("key.lockitem", Keyboard.KEY_R, "key.itemlock.category");
	
	@Override
	public void setupKeybind() {
		ClientRegistry.registerKeyBinding(KEY_LOCK_ITEM);
	}
}
