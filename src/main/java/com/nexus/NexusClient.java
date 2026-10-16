package com.nexus;

import com.mojang.blaze3d.platform.InputConstants;
import com.nexus.gui.ClickGui;
import com.nexus.module.ModuleManager;
import com.nexus.util.Commands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("nexus");
    public static ModuleManager modules;
    private static KeyMapping guiKey;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();

        guiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.nexus.gui", InputConstants.Type.KEYSYM, InputConstants.KEY_RSHIFT, KeyMapping.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            modules.tick();
            while (guiKey.consumeClick()) {
                if (client.screen == null && client.level != null) client.setScreen(new ClickGui());
            }
        });

        Commands.register();
        LOG.info("Nexus Client zaladowany: {} modulow", modules.all().size());
    }
}
