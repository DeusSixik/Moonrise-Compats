package dev.sixik.moonrise_compats.mixin.fixes.ice_and_fire;

import ca.spottedleaf.moonrise.patches.chunk_system.world.ChunkSystemServerChunkCache;
import com.iafenvoy.uranus.world.WorldChunkUtil;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldChunkUtil.class)
public class IceAndFire$WorldChunkUtilMixin {


    /**
     * @author Sixik
     * @reason Fix crash when use getFullChunkFuture
     */
    @Overwrite
    public static boolean isChunkLoaded(LevelAccessor world, int x, int z) {
        ChunkSource var4 = world.getChunkSource();
        if (var4 instanceof ServerChunkCache serverChunkManager && serverChunkManager instanceof ChunkSystemServerChunkCache chunkCache) {
            LevelChunk chunk = chunkCache.moonrise$getFullChunkIfLoaded(x, z);
            return chunk != null;
        } else {
            return world.getChunk(x, z, ChunkStatus.FULL, false) != null;
        }
    }
}
