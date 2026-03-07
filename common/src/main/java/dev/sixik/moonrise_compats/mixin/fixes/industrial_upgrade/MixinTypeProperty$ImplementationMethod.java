package dev.sixik.moonrise_compats.mixin.fixes.industrial_upgrade;

import ca.spottedleaf.moonrise.patches.blockstate_propertyaccess.PropertyAccess;
import com.denfop.blocks.state.State;
import com.denfop.blocks.state.TypeProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(TypeProperty.class)
public abstract class MixinTypeProperty$ImplementationMethod extends Property<State> implements PropertyAccess<State> {


    @Shadow
    public List<State> allowedValues;

    @Unique
    private Map<State, Integer> moonrise$idLookup;

    // Конструктор-заглушка для компилятора Mixin
    protected MixinTypeProperty$ImplementationMethod(String name, Class<State> clazz) {
        super(name, clazz);
    }

    /**
     * Hot path для Moonrise. Вызывается часто при получении ID состояния.
     */
    @Override
    public final int moonrise$getIdFor(final State value) {
        // Избегаем NullPointerException при автоанбоксинге
        Integer id = this.moonrise$idLookup.get(value);
        return id != null ? id : -1;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMoonriseLookup(CallbackInfo ci) {
        int size = this.allowedValues.size();

        // Оптимизация: задаем точный размер и load factor 1.0f, чтобы избежать rehashing
        this.moonrise$idLookup = new HashMap<>(size, 1.0f);
        State[] byId = new State[size];

        int id = 0;
        for (State state : this.allowedValues) {
            this.moonrise$idLookup.put(state, id);
            byId[id++] = state;
        }

        this.moonrise$setById(byId);
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }
}
