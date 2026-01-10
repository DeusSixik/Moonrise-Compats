package dev.sixik.moonrise_compats.mixin_core;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class MoonriseCompatsMixinCanceller implements MixinCanceller {

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        for (MixinApplier mixinApplier : MoonriseCompatsMixinPlugin.MixinAppliers) {
            if(mixinApplier.hasDisableMixin(mixinClassName) && mixinApplier.isModLoaded())
                return true;
        }

        return false;
    }
}
