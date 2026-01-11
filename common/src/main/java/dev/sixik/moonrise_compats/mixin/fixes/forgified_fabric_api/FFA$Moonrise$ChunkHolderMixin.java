package dev.sixik.moonrise_compats.mixin.fixes.forgified_fabric_api;

import ca.spottedleaf.moonrise.patches.chunk_system.scheduling.NewChunkHolder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.impl.event.lifecycle.ChunkLevelTypeEventTracker;
import net.minecraft.server.level.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.minecraft.server.level.FullChunkStatus.*;
import static net.minecraft.server.level.FullChunkStatus.BLOCK_TICKING;
import static net.minecraft.server.level.FullChunkStatus.ENTITY_TICKING;

@Mixin(value = NewChunkHolder.class, priority = 1500)
public abstract class FFA$Moonrise$ChunkHolderMixin implements ChunkLevelTypeEventTracker {

    @Shadow @Final public ServerLevel world;
    @Shadow public abstract ChunkAccess getChunkIfPresentUnchecked(ChunkStatus status);

    @Unique
    private FullChunkStatus fabric_currentEventLevelType = INACCESSIBLE;

    // INACCESSIBLE -> FULL
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void onPromoteToFull(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(INACCESSIBLE, FULL);
    }

    // FULL -> BLOCK_TICKING
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onPromoteToBlockTicking(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(FULL, BLOCK_TICKING);
    }

    // BLOCK_TICKING -> ENTITY_TICKING
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void onPromoteToEntityTicking(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(BLOCK_TICKING, ENTITY_TICKING);
    }

    // ENTITY_TICKING -> BLOCK_TICKING
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 3))
    private void onDemoteToBlockTicking(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(ENTITY_TICKING, BLOCK_TICKING);
    }

    // BLOCK_TICKING -> FULL
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 4))
    private void onDemoteToFull(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(BLOCK_TICKING, FULL);
    }

    // FULL -> INACCESSIBLE
    @Inject(method = "handleFullStatusChange",
            at = @At(value = "INVOKE", target = "Lca/spottedleaf/moonrise/patches/chunk_system/scheduling/NewChunkHolder;updateCurrentState(Lnet/minecraft/server/level/FullChunkStatus;)V", ordinal = 5))
    private void onDemoteToInaccessible(List<NewChunkHolder> changedFullStatus, CallbackInfoReturnable<Boolean> cir) {
        fireEvent(FULL, INACCESSIBLE);
    }

    @Unique
    private void fireEvent(FullChunkStatus from, FullChunkStatus to) {
        if (this.fabric_currentEventLevelType == from) {
            ChunkAccess chunkAccess = this.getChunkIfPresentUnchecked(ChunkStatus.FULL);
            if (chunkAccess instanceof LevelChunk chunk) {
                ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange(this.world, chunk, from, to);
                this.fabric_currentEventLevelType = to;
            }
        }
    }

    @Override
    public void fabric_setCurrentEventLevelType(FullChunkStatus levelType) {
        this.fabric_currentEventLevelType = levelType;
    }

    @Override
    public FullChunkStatus fabric_getCurrentEventLevelType() {
        return this.fabric_currentEventLevelType;
    }
}
