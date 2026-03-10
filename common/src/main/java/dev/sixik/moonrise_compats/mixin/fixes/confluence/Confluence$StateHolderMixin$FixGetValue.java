package dev.sixik.moonrise_compats.mixin.fixes.confluence;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.mod.common.data.saved.GlobalCloakData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StateHolder.class, priority = 1500)
public abstract class Confluence$StateHolderMixin$FixGetValue<O, S> implements SelfGetter<StateHolder<O, S>> {

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.blockstate_propertyaccess.StateHolderMixin",
            name = "getValue"
    )
    @Inject(
            method = {"getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"},
            at = {@At(
                    value = "NEW",
                    target = "Ljava/lang/IllegalArgumentException;"
            )},
            cancellable = true
    )
    private <T extends Comparable<T>> void test(Property<T> property, CallbackInfoReturnable<T> cir) {
        Object var4 = this.confluence$self();
        if (var4 instanceof BlockState source) {
            BlockState target = GlobalCloakData.INSTANCE.getTarget(source);
            if (target != source) {
                cir.setReturnValue(target.getValue(property));
            }
        }

    }
}
