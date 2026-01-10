package dev.sixik.moonrise_compats.mixin.fixes.immersive_optimization;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.conczin.immersive_optimization.TickScheduler;
import net.conczin.immersive_optimization.config.Config;
import net.conczin.immersive_optimization.mixin.ServerLevelAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TickScheduler.class)
public abstract class MixinTickScheduler$FixEntityList {

    @Shadow
    @Final
    public Map<ResourceLocation, TickScheduler.LevelData> levelData;

    @Shadow
    public abstract int getPriority(TickScheduler.LevelData data, ServerLevel level, Entity entity);

    /**
     * @author Sixik
     * @reason Fix entity iterations
     */
    @Overwrite
    void tickLevel(ServerLevel level) {
        TickScheduler.LevelData data = this.levelData.computeIfAbsent(level.dimension().location(), TickScheduler.LevelData::new);
        long tick = level.getGameTime();
        TickScheduler.Stats previousStats = data.previousStats;
        data.previousStats = data.stats;
        data.stats = previousStats;
        data.stats.reset();
        if (tick % 207L == 0L) {
            data.blockEntityPriorities.clear();
        }

        if (!Config.getInstance().enableEntities) {
            data.priorities.clear();
        } else {
            Int2IntOpenHashMap newPriorities = new Int2IntOpenHashMap();
            ((ServerLevelAccessor)level).getEntityTickList().forEach((entity) -> {
                if (entity != null) {
                    int priority = this.getPriority(data, level, entity);
                    if (priority > 0) {
                        newPriorities.put(entity.getId(), priority);
                    }

                    TickScheduler.Stats stats = data.stats;
                    stats.tickRate += (double)1.0F / (double)Math.max(1, priority);
                    ++data.stats.entities;
                }

            });
            data.priorities = newPriorities;
        }
    }
}
