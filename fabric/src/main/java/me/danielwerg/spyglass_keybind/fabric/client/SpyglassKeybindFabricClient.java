package me.danielwerg.spyglass_keybind.fabric.client;

import me.danielwerg.spyglass_keybind.client.SpyglassKeybindClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public final class SpyglassKeybindFabricClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    KeyBindingHelper.registerKeyBinding(SpyglassKeybindClient.useSpyglass);

    ClientTickEvents.END_CLIENT_TICK.register(
        client -> SpyglassKeybindClient.getInstance().onClientTick(client));
  }
}
