package dev.sixik.moonrise_compats.commons;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import ca.spottedleaf.moonrise.common.list.ReferenceList;
import ca.spottedleaf.moonrise.common.util.ChunkSystem;
import ca.spottedleaf.moonrise.common.util.CoordinateUtils;
import ca.spottedleaf.moonrise.common.util.TickThread;
import ca.spottedleaf.moonrise.patches.chunk_system.level.ChunkSystemServerLevel;
import ca.spottedleaf.moonrise.patches.chunk_system.level.entity.ChunkEntitySlices;
import ca.spottedleaf.moonrise.patches.chunk_system.level.entity.EntityLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelCallback;

import java.util.ArrayList;
import java.util.List;

public class FakeEntityLookup extends EntityLookup {
    private final ServerLevel serverWorld;
    public final ReferenceList<Entity> trackerEntities;

    public FakeEntityLookup(ServerLevel world, LevelCallback<Entity> worldCallback, Entity[] entities) {
        super(world, worldCallback);
        this.trackerEntities = new ReferenceList(entities);
        this.serverWorld = world;
    }

    protected Boolean blockTicketUpdates() {
        return ((ChunkSystemServerLevel)this.serverWorld).moonrise$getChunkTaskScheduler().chunkHolderManager.blockTicketUpdates();
    }

    protected void setBlockTicketUpdates(Boolean value) {
        ((ChunkSystemServerLevel)this.serverWorld).moonrise$getChunkTaskScheduler().chunkHolderManager.unblockTicketUpdates(value);
    }

    protected void checkThread(int chunkX, int chunkZ, String reason) {
        TickThread.ensureTickThread(this.serverWorld, chunkX, chunkZ, reason);
    }

    protected void checkThread(Entity entity, String reason) {
        TickThread.ensureTickThread(entity, reason);
    }

    protected ChunkEntitySlices createEntityChunk(int chunkX, int chunkZ, boolean transientChunk) {
        return ((ChunkSystemServerLevel)this.serverWorld).moonrise$getChunkTaskScheduler().chunkHolderManager.getOrCreateEntityChunk(chunkX, chunkZ, transientChunk);
    }

    protected void onEmptySlices(int chunkX, int chunkZ) {
    }

    protected void entitySectionChangeCallback(Entity entity, int oldSectionX, int oldSectionY, int oldSectionZ, int newSectionX, int newSectionY, int newSectionZ) {
        if (entity instanceof ServerPlayer player) {
            ((ChunkSystemServerLevel)this.serverWorld).moonrise$getNearbyPlayers().tickPlayer(player);
        }

        PlatformHooks.get().entityMove(entity, CoordinateUtils.getChunkSectionKey(oldSectionX, oldSectionY, oldSectionZ), CoordinateUtils.getChunkSectionKey(newSectionX, newSectionY, newSectionZ));
    }

    protected void addEntityCallback(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            ((ChunkSystemServerLevel)this.serverWorld).moonrise$getNearbyPlayers().addPlayer(player);
        }

    }

    protected void removeEntityCallback(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            ((ChunkSystemServerLevel)this.serverWorld).moonrise$getNearbyPlayers().removePlayer(player);
        }

    }

    protected void entityStartLoaded(Entity entity) {
        this.trackerEntities.add(entity);
    }

    protected void entityEndLoaded(Entity entity) {
        this.trackerEntities.remove(entity);
    }

    protected void entityStartTicking(Entity entity) {
    }

    protected void entityEndTicking(Entity entity) {
    }

    protected boolean screenEntity(Entity entity, boolean fromDisk, boolean event) {
        return ChunkSystem.screenEntity(this.serverWorld, entity, fromDisk, event);
    }

    public void clearEntities() {
        this.trackerEntities.clear();
    }

    public List<Entity> getEntittiesList() {
        List<Entity> entities = new ArrayList<>();
        this.trackerEntities.forEach(entities::add);
        return entities;
    }
}
