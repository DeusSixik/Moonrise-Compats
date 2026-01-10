package dev.sixik.moonrise_compats.mixin.fixes.citadel;

import ca.spottedleaf.moonrise.patches.chunk_system.world.ChunkSystemServerChunkCache;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.ChunkCache;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class Citadel$ChunkCache$FixGetFullChunkFutures {

    @Shadow
    protected LevelChunk[][] chunkArray;

    @Shadow
    protected int chunkX;

    @Shadow
    protected int chunkZ;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;getVisibleChunkIfPresent(J)Lnet/minecraft/server/level/ChunkHolder;"))
    public ChunkHolder bts$init$nullable_holder(ChunkMap instance, long chunkPos) {
        return null;
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;getVisibleChunkIfPresent(J)Lnet/minecraft/server/level/ChunkHolder;", shift = At.Shift.AFTER))
    public void bts$init$redirect_holder(Level worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn,
                                         DimensionType type, CallbackInfo ci, @Local(argsOnly = true) int k, @Local(argsOnly = true) int l, @Local ServerChunkCache serverChunkCache) {
        if(serverChunkCache instanceof ChunkSystemServerChunkCache chunkCache) {
            LevelChunk chunk = chunkCache.moonrise$getFullChunkIfLoaded(k, l);
            if(chunk == null) return;
            this.chunkArray[k - this.chunkX][l - this.chunkZ] = chunk;
        }
    }
}
