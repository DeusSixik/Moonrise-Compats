package dev.sixik.moonrise_compats.mixin.fixes.amendments;

import ca.spottedleaf.moonrise.patches.collisions.ExplosionBlockCache;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.amendments.common.entity.FireballExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Explosion.class, priority = 1500)
public class Amendments$Moonrise$ExplosionMixin {

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.ExplosionMixin",
            name = "explode"
    )
    @WrapOperation(
            method = {"@MixinSquared:Handler"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )}
    )
    public boolean amendments$HurtWithContext(Entity entity, DamageSource source, float amount, Operation<Boolean> original) {
        if ((Object) this instanceof FireballExplosion fe) {
            return fe.hurtHitEntity(entity, source, amount);
        } else {
            return original.call(entity, source, amount);
        }
    }

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.ExplosionMixin",
            name = "explode"
    )
    @WrapWithCondition(
            method = {"@MixinSquared:Handler"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )}
    )
    public boolean amendments$cancelKnockback(Entity instance, Vec3 deltaMovement) {

        if ((Object) this instanceof FireballExplosion fe) {
            if (!fe.hasKnockback()) {
                return false;
            }
        }

        return true;
    }

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.ExplosionMixin",
            name = "getOrCacheExplosionBlock"
    )
    @Inject(
            method = {"@MixinSquared:Handler"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;getBlockExplosionResistance(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Ljava/util/Optional;"
            )}
    )
    public void amendments$addBlockSideEffects(int x, int y, int z, long key, boolean calculateResistance, CallbackInfoReturnable<ExplosionBlockCache> cir, @Local(name = "pos") BlockPos pos, @Local(name = "blockState") BlockState state) {
        if ((Object) this instanceof FireballExplosion fe) {
            fe.addVisitedBlock(pos, state);
        }

    }
}
