package dev.sixik.moonrise_compats.fabric;

import dev.sixik.moonrise_compats.Moonrise_compats;
import net.fabricmc.api.ModInitializer;

public final class Moonrise_compatsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Moonrise_compats.init();
    }
}
