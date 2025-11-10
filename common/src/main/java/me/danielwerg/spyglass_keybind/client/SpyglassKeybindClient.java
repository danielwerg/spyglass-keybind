package me.danielwerg.spyglass_keybind.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.danielwerg.spyglass_keybind.mixin.MinecraftClientInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SpyglassKeybindClient {
  public static final String MOD_ID = "spyglass_keybind";

  private static SpyglassKeybindClient INSTANCE;

  public static SpyglassKeybindClient getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SpyglassKeybindClient();
    }
    return INSTANCE;
  }

  public static final Logger LOGGER = LogManager.getLogger();

  public static int spyglassSlot = -1;
  public static int originalHotbarSelectedSlot = -1;
  public static int activeHotbarSelectedSlot = -1;
  public static boolean interrupted = false;

  public static final KeyMapping.Category spyglass_keybind = KeyMapping.Category
      .register(ResourceLocation.fromNamespaceAndPath("spyglass-keybind",
          "spyglass"));

  public static KeyMapping useSpyglass = new KeyMapping(
      "key.spyglass-keybind.use",
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_Z,
      spyglass_keybind);

  public void init() {
    INSTANCE = this;
    LOGGER.info("Spyglass Keybind Client Initialized");
  }

  public void onClientTick(Minecraft client) {
    if (client.player == null || client.gameMode == null)
      return;

    if (client.screen != null) {
      if (client.screen.shouldCloseOnEsc()) {
        interrupted = true;
      } else {
        interrupted = false;
      }
    }

    LocalPlayer player = client.player;

    if (useSpyglass.isDown()
        && ((MinecraftClientInvoker) client).getItemUseCooldown() == 0
        && !client.player.isUsingItem()) {

      spyglassSlot = findSlotByItem(client.player.getInventory(), Items.SPYGLASS);

      if (player.getMainHandItem().getItem().equals(Items.SPYGLASS)) {
        // in main hand
        originalHotbarSelectedSlot = spyglassSlot;
        client.gameMode.useItem(player, InteractionHand.MAIN_HAND);
      } else if (player.getOffhandItem().getItem().equals(Items.SPYGLASS)) {
        // in off hand
        originalHotbarSelectedSlot = spyglassSlot;
        client.gameMode.useItem(player, InteractionHand.OFF_HAND);
      } else if (spyglassSlot >= 0 && spyglassSlot <= 8) {
        // in hotbar
        originalHotbarSelectedSlot = player.getInventory().getSelectedSlot();
        player.getInventory().setSelectedSlot(spyglassSlot);
        client.gameMode.useItem(player, InteractionHand.MAIN_HAND);
      } else if (spyglassSlot >= 9 && spyglassSlot <= 35) {
        // in inventory
        client.gameMode.handleInventoryMouseClick(
            0, spyglassSlot, 40, ClickType.SWAP, player);
        client.gameMode.useItem(player, InteractionHand.OFF_HAND);
      }
    }
  }

  /**
   * Finds a slot containing an itemstack of the given item type.
   * 
   * @param inventory - Players inventory.
   * @param item      - Item type to search for.
   * @return Slot ID, -1 if item was not found.
   */
  private int findSlotByItem(Inventory inventory, Item item) {
    List<ItemStack> items = inventory.getNonEquipmentItems();
    for (int i = 0; i < items.size(); ++i) {
      if (!items.get(i).isEmpty() && items.get(i).is(item)) {
        return i;
      }
    }
    return -1;
  }
}
