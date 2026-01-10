package dev.sixik.moonrise_compats.mixin.fixes.amendments;

import net.mehvahdjukaar.amendments.common.entity.FireballExplosion;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Explosion.class)
public class Amendments$ExplosionMixin {

    @ModifyArg(
            method = {"finalizeExplosion(Z)V"},
            index = 5,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            )
    )
    public float amendments$changeSoundVolume(float volume) {
        if ((Object) this instanceof FireballExplosion fe) {
            return fe.getExplosionVolume();
        } else {
            return volume;
        }
    }
}
