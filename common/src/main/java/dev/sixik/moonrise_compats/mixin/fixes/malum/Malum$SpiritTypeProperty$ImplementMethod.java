package dev.sixik.moonrise_compats.mixin.fixes.malum;

import ca.spottedleaf.moonrise.patches.blockstate_propertyaccess.PropertyAccess;
import com.google.common.collect.ImmutableSet;
import com.sammy.malum.core.systems.spirit.SpiritTypeProperty;
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
    private Map<String, Integer> moonrise$idLookup;

    protected Malum$SpiritTypeProperty$ImplementMethod(String name, Class<String> clazz) {
        super(name, clazz);
    }

    @Override
    public final int moonrise$getIdFor(final String value) {
        Integer id = moonrise$idLookup.get(value);
        return id != null ? id : -1;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMoonriseLookup(CallbackInfo ci) {
        this.moonrise$idLookup = new HashMap<>();
        String[] moonrise$byId = new String[this.values.size()];

        int id = 0;
        for (String v : this.values) {
            moonrise$idLookup.put(v, id);
            moonrise$byId[id++] = v;
        }

        this.moonrise$setById(moonrise$byId);
    }

    /**
     * Identity equals (как у всех Moonrise PropertyMixin)
     */
    @Overwrite
    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
}
