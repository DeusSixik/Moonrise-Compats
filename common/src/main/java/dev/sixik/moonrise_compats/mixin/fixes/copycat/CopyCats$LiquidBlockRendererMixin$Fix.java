package dev.sixik.moonrise_compats.mixin.fixes.copycat;

import com.bawnorton.mixinsquared.TargetHandler;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LiquidBlockRenderer.class, priority = 1500)
public class CopyCats$LiquidBlockRendererMixin$Fix {

    @TargetHandler(
            mixin = "ca.spottedleaf.moonrise.mixin.collisions.LiquidBlockRendererMixin",
            name = "isFaceOccludedByState"
    )
    @WrapOperation(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z")
    )
    private static boolean isFaceOccludedByState$canCopycatOcclude(BlockState instance, Operation<Boolean> original, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true) BlockPos pos) {
        if (AllBlocks.COPYCAT_BASE.has(instance)) {
            return false;
        } else if (instance.getBlock() instanceof BracketBlock) {
            return false;
        } else {
            final Block var5 = instance.getBlock();
            if (var5 instanceof ICopycatBlock copycatBlock) {
                return copycatBlock.canOcclude(level, instance, pos);
            } else {
                return original.call(instance);
            }
        }
    }
}
