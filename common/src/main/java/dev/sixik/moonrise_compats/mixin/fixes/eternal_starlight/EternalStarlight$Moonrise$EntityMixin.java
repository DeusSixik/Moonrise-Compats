package dev.sixik.moonrise_compats.mixin.fixes.eternal_starlight;

import cn.leolezury.eternalstarlight.common.registry.ESDataAttachments;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESMobEffects;
import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 1500)
public abstract class EternalStarlight$Moonrise$EntityMixin {

    @Shadow
    public int tickCount;
    @Unique
    private boolean feetInWater = false;

    @Shadow
    public abstract boolean isInWater();

    @Shadow
    public abstract Level level();

    @Shadow
    public abstract AABB getBoundingBox();

    @Inject(
            method = {"isStateClimbable(Lnet/minecraft/world/level/block/state/BlockState;)Z"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void isStateClimbable(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        final Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity living) {
            if (living.hasEffect(ESMobEffects.STICKY.asHolder())) {
                cir.setReturnValue(true);
            }
        }

    }

    @Inject(
            method = {"tick()V"},
            at = {@At("RETURN")}
    )
    private void tick(CallbackInfo ci) {
        this.feetInWater = this.isInWater() && this.level().getFluidState(BlockPos.containing(this.getBoundingBox().getBottomCenter())).is(FluidTags.WATER);
        final Entity entity = (Entity) (Object)this;
        if (this.level().isClientSide && this.feetInWater && entity instanceof LivingEntity living) {
            if (living.getDeltaMovement().length() > 0.01 && living.getItemBySlot(EquipmentSlot.FEET).is(ESItems.AIR_SAC_BOOTS.get())) {
                 final Vec3 pos = living.getBoundingBox().getBottomCenter().offsetRandom(living.getRandom(), living.getBbWidth());
                 final Vec3 speed = living.getDeltaMovement().normalize().offsetRandom(living.getRandom(), 0.3F).scale(-0.2);
                this.level().addParticle(ESParticles.ROOKFISH_INK.get(), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
            }
        }

    }

    @Inject(
            method = {"getGravity()D"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void getGravity(CallbackInfoReturnable<Double> cir) {
        final Entity entity = (Entity) (Object)this;
        if (entity instanceof LivingEntity living) {
            if (this.feetInWater && living.getItemBySlot(EquipmentSlot.FEET).is((Item)ESItems.AIR_SAC_BOOTS.get())) {
                cir.setReturnValue((double)0.0F);
            }
        }

    }

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.EntityMixin",
            name = "checkInsideBlocks"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;entityInside(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V"
            )}
    )
    private void checkInsideBlocks(CallbackInfo ci, @Local(name = "blockState") BlockState state) {
        if (state.getFluidState().is(ESTags.Fluids.ETHER)) {
            ESDataAttachments.IN_ETHER.setData((Entity) (Object)this, true);
        }

    }

    @Inject(
            method = {"deflection(Lnet/minecraft/world/entity/projectile/Projectile;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void deflection(Projectile projectile, CallbackInfoReturnable<ProjectileDeflection> cir) {
        final Entity entity = (Entity) (Object)this;
        if (entity instanceof LivingEntity living) {
            if (living.isBlocking() && living.getUseItem().is(ESItems.FLOWGLAZE_SHIELD.get())) {
                final Vec3 viewVector = living.calculateViewVector(0.0F, living.getYHeadRot());
                Vec3 offset = projectile.position().vectorTo(living.position());
                offset = (new Vec3(offset.x, 0.0F, offset.z)).normalize();
                if (offset.dot(viewVector) < (double)0.0F) {
                    cir.setReturnValue(ProjectileDeflection.AIM_DEFLECT);
                }
            }
        }

    }

    @Inject(
            method = {"move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V",
                    ordinal = 1
            )}
    )
    private void move(MoverType moverType, Vec3 vec3, CallbackInfo ci, @Local(ordinal = 1) Vec3 movement) {
        final Entity entity = (Entity) (Object)this;
        int lastUpdate = ESDataAttachments.LAST_MOVEMENT_UPDATE.getData(entity);
        if (lastUpdate != this.tickCount) {
            ESDataAttachments.MOVEMENT.setData(entity, movement);
            ESDataAttachments.LAST_MOVEMENT_UPDATE.setData(entity, this.tickCount);
        } else {
            ESDataAttachments.MOVEMENT.setData(entity, ((Vec3)ESDataAttachments.MOVEMENT.getData(entity)).add(movement));
        }

    }
}
