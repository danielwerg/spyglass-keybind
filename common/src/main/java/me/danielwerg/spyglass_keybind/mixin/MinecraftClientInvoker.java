package me.danielwerg.spyglass_keybind.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftClientInvoker {

    @Accessor("rightClickDelay")
    int getItemUseCooldown();

}
