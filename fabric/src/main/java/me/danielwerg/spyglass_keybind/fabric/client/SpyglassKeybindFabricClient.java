package me.danielwerg.spyglass_keybind.fabric.client;

import me.danielwerg.spyglass_keybind.client.SpyglassKeybindClient;
import me.danielwerg.spyglass_keybind.client.integrations.IEquipmentIntegration;
import me.danielwerg.spyglass_keybind.fabric.client.integrations.TrinketsIntegration;
import me.danielwerg.spyglass_keybind.integrations.AccessoriesIntegration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class SpyglassKeybindFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SpyglassKeybindClient.useSpyglass);
        IEquipmentIntegration integration = null;
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            integration = new AccessoriesIntegration();
        } else if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            integration = new TrinketsIntegration();
        }

        SpyglassKeybindClient.getInstance().init(integration);

        ClientTickEvents.END_CLIENT_TICK.register(client -> SpyglassKeybindClient.getInstance().onClientTick(client));
    }
}
