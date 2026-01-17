package dev.sixik.moonrise_compats.mixin.fixes.vinery;

import ca.spottedleaf.moonrise.patches.blockstate_propertyaccess.PropertyAccess;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.state.properties.Property;
import net.satisfy.vinery.core.block.state.properties.GrapeProperty;
import net.satisfy.vinery.core.util.GrapeType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(value = GrapeProperty.class, remap = false)
public abstract class Vinery$GrapeProperty$ImplementMethod extends Property<GrapeType> implements PropertyAccess<GrapeType> {

    @Shadow
    @Final
    private Set<GrapeType> values;
    @Unique
    private Map<GrapeType, Integer> moonrise$idLookup;

    protected Vinery$GrapeProperty$ImplementMethod(String string, Class<GrapeType> class_) {
        super(string, class_);
    }


    @Override
    public final int moonrise$getIdFor(final GrapeType value) {
        Integer id = moonrise$idLookup.get(value);
        return id != null ? id : -1;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initMoonriseLookup(CallbackInfo ci) {
        this.moonrise$idLookup = new HashMap<>();
        GrapeType[] moonrise$byId = new GrapeType[this.values.size()];

        int id = 0;
        for (GrapeType v : this.values) {
            moonrise$idLookup.put(v, id);
            moonrise$byId[id++] = v;
        }

        this.moonrise$setById(moonrise$byId);
    }

    /**
     * Identity equals (как у всех Moonrise PropertyMixin)
     */
    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
}
