package dev.sixik.moonrise_compats.neoforge;

import dev.sixik.moonrise_compats.MoonriseCompats;
import net.neoforged.fml.common.Mod;

@Mod(MoonriseCompats.MOD_ID)
public final class MoonriseCompatsNeoForge {
    public MoonriseCompatsNeoForge() {
        // Run our common setup.
        MoonriseCompats.init();
    }
}
