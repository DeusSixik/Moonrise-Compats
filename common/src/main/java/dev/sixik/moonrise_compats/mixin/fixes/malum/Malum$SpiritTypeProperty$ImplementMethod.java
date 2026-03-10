package dev.sixik.moonrise_compats.mixin.fixes.malum;

import ca.spottedleaf.moonrise.patches.blockstate_propertyaccess.PropertyAccess;
import com.google.common.collect.ImmutableSet;
import com.sammy.malum.core.systems.spirit.SpiritTypeProperty;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(SpiritTypeProperty.class)
public abstract class Malum$SpiritTypeProperty$ImplementMethod extends Property<String> implements PropertyAccess<String> {

    @Final
    @Shadow
    private ImmutableSet<String> values;

    @Unique
    private Object2IntOpenHashMap<String> moonrise$idLookup;

    protected Malum$SpiritTypeProperty$ImplementMethod(String name, Class<String> clazz) {
        super(name, clazz);
    }

    @Override
    public final int moonrise$getIdFor(final String value) {
        return this.moonrise$idLookup.getOrDefault(value, -1);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initMoonriseLookup(CallbackInfo ci) {
        this.moonrise$idLookup = new Object2IntOpenHashMap<>(this.values.size());
        this.moonrise$idLookup.defaultReturnValue(-1);

        String[] moonrise$byId = new String[this.values.size()];

        int id = 0;
        for (String v : this.values) {
            this.moonrise$idLookup.put(v, id);
            moonrise$byId[id++] = v;
        }

        try {
            this.moonrise$setById(moonrise$byId);
        } catch (IllegalStateException ignored) { }
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
}
