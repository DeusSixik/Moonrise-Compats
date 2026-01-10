package dev.sixik.moonrise_compats.mixin.fixes.moonrise;

import com.bawnorton.mixinsquared.TargetHandler;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = StateHolder.class, priority = 1500)
public class StateHolderMixin$FixNullReference{

    @Mutable
    @Shadow
    @Final
    private Reference2ObjectArrayMap<Property<?>, Comparable<?>> values;

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.blockstate_propertyaccess.StateHolderMixin",
            name = "init"
    )
    @Redirect(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Reference2ObjectArrayMap;keySet()Lit/unimi/dsi/fastutil/objects/ReferenceSet;"))
    private <K> ReferenceSet<K> onInit$fix_null(Reference2ObjectArrayMap<K, Comparable<?>> instance) {
        if(instance == null) {
            values = new Reference2ObjectArrayMap<>();
            return (ReferenceSet<K>) values.keySet();
        }
        return instance.keySet();
    }
}
