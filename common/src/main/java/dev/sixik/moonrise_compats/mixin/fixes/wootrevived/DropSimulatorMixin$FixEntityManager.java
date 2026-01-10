package dev.sixik.moonrise_compats.mixin.fixes.wootrevived;

import ca.spottedleaf.moonrise.patches.chunk_system.level.ChunkSystemLevel;
import dev.sixik.moonrise_compats.commons.EntityLookupPath;
import dev.sixik.moonrise_compats.commons.FakeEntityLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wootrevived.woot.drops.simulator.DropSimulator;
import wootrevived.woot.drops.simulator.FakeEntityManager;

import java.util.List;

@Mixin(DropSimulator.class)
public class DropSimulatorMixin$FixEntityManager {

    @Unique
    private FakeEntityLookup bts$fakeEntityLookup;

    @Shadow
    private ServerLevel dimensionLevel;

    @Redirect(method = "simulate", at = @At(value = "INVOKE", target = "Lwootrevived/woot/drops/simulator/FakeEntityManager;clearEntityList()V"))
    private void simulate$redirect_1(FakeEntityManager instance) {
        bts$fakeEntityLookup.clearEntities();
    }

    @Redirect(method = "simulateChargedCreeper", at = @At(value = "INVOKE", target = "Lwootrevived/woot/drops/simulator/FakeEntityManager;clearEntityList()V"))
    private void simulateChargedCreeper$redirect_1(FakeEntityManager instance) {
        bts$fakeEntityLookup.clearEntities();
    }

    @Redirect(method = "simulateChargedCreeper", at = @At(value = "INVOKE", target = "Lwootrevived/woot/drops/simulator/FakeEntityManager;getEntityList()Ljava/util/List;"))
    private List<? extends Entity> simulateChargedCreeper$redirect_2(FakeEntityManager instance) {;
        return bts$fakeEntityLookup.getEntittiesList();
    }

    @Redirect(method = "simulateEnderdragon", at = @At(value = "INVOKE", target = "Lwootrevived/woot/drops/simulator/FakeEntityManager;clearEntityList()V"))
    private void simulateEnderdragon$redirect_1(FakeEntityManager instance) {
        bts$fakeEntityLookup.clearEntities();
    }

    @Redirect(method = "simulateEnderdragon", at = @At(value = "INVOKE", target = "Lwootrevived/woot/drops/simulator/FakeEntityManager;getEntityList()Ljava/util/List;"))
    private List<? extends Entity> simulateEnderdragon$redirect_2(FakeEntityManager instance) {;
        return bts$fakeEntityLookup.getEntittiesList();
    }

    /**
     * @author Sixik
     * @reason
     */
    @Overwrite(remap = false)
    private void patchDimensionLevel() {
        if(this.dimensionLevel instanceof ChunkSystemLevel chunkSystemLevel && chunkSystemLevel.moonrise$getEntityLookup() instanceof EntityLookupPath path) {
            this.bts$fakeEntityLookup = new FakeEntityLookup(this.dimensionLevel, path.bts$getCallBack(), path.bts$getAllEntities());
        }
    }
}
