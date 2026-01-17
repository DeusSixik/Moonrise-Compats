package dev.sixik.moonrise_compats.mixin.vanilla;

import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrontAndTop.class)
public abstract class MixinFrontAndTopNullGuard {
    private static final org.slf4j.Logger LOG =
            com.mojang.logging.LogUtils.getLogger();

    @Inject(method = "fromFrontAndTop", at = @At("HEAD"), cancellable = true)
    private static void dbg(Direction front, Direction top,
                            CallbackInfoReturnable<FrontAndTop> cir) {
        if (front == null || top == null) {
            LOG.error("[MOONRISE COMPATS] [PLEASE NOTIFY THIS TO ME] FrontAndTop.fromFrontAndTop got NULL: front={}, top={}",
                    front, top, new Throwable("trace"));
            cir.setReturnValue(FrontAndTop.NORTH_UP);
        }
    }
}
