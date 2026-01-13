package dev.sixik.moonrise_compats.mixin.fixes.blueprint;

import com.teamabnormals.blueprint.common.world.storage.GlobalStorageManager;
import com.teamabnormals.blueprint.common.world.storage.receiver.BlueprintServerLevel;
import com.teamabnormals.blueprint.common.world.storage.receiver.LevelDataReceiver;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin({ServerLevel.class})
public abstract class Blueprint$ServerLevelMixin extends Level implements WorldGenLevel, BlueprintServerLevel {
    @Unique
    private Object[] bts$levelData;

    @Unique
    private ResourceKey<Level> bts$resourceKey;

    protected Blueprint$ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Inject(
            method = {"<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V"},
            at = {@At("RETURN")}
    )
    private void init(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess levelSave, ServerLevelData serverWorldInfo, ResourceKey<Level> resourceKey, LevelStem levelStem, ChunkProgressListener statusListener, boolean bl, long l, List<CustomSpawner> list, boolean bl2, @Nullable RandomSequences randomSequences, CallbackInfo info) {
        this.bts$resourceKey = resourceKey;
        bts$create();
    }

    public Object getLevelData(int index) {
        if(index >= bts$levelData.length)
            bts$create();

        return this.bts$levelData[index];
    }

    @Unique
    private void bts$create() {
        if (bts$resourceKey == Level.OVERWORLD) {
            GlobalStorageManager.getOrCreate((ServerLevel)(Object)this);
        }

        List<LevelDataReceiver<?>> receivers = LevelDataReceiver.getReceivers();
        this.bts$levelData = new Object[receivers.size()];

        for(int i = 0; i < receivers.size(); ++i) {
            this.bts$levelData[i] = receivers.get(i).create((ServerLevel)(Object)this);
        }
    }
}
