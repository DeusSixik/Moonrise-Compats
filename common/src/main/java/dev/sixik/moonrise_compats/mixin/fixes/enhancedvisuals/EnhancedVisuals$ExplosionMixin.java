package dev.sixik.moonrise_compats.mixin.fixes.enhancedvisuals;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.creative.enhancedvisuals.EnhancedVisuals;

import java.util.List;

@Mixin(value = Explosion.class, priority = 1500)
public class EnhancedVisuals$ExplosionMixin {

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.ExplosionMixin",
            name = "explode"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    public List<Entity> afterMoonrise$enhancedvisuals$onDetonate(Level instance, Entity entity, AABB aabb) {
        List<Entity> list = instance.getEntities(entity, aabb);
        EnhancedVisuals.EVENTS.explosion((Explosion)(Object) this, list);
        return list;
    }
}
