package dev.sixik.moonrise_compats.mixin.fixes.ice_and_fire;

import ca.spottedleaf.moonrise.patches.chunk_system.level.ChunkSystemLevel;
import ca.spottedleaf.moonrise.patches.chunk_system.level.entity.EntityLookup;
import com.iafenvoy.iceandfire.data.component.ChainData;
import com.iafenvoy.iceandfire.data.component.MiscData;
import com.iafenvoy.iceandfire.event.ClientEvents;
import com.iafenvoy.iceandfire.registry.IafStatusEffects;
import com.iafenvoy.iceandfire.render.misc.ChainRenderer;
import com.iafenvoy.iceandfire.render.misc.CockatriceBeamRenderer;
import com.iafenvoy.iceandfire.render.misc.FrozenStateRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;
import java.util.UUID;

@Mixin(ClientEvents.class)
public class IceAndFire$ClientEvents$FixEntityStorage {

    /**
     * @author Sixik
     * @reason
     */
    @Overwrite
    public static void onPostRenderLiving(LivingEntity entity, float partialRenderTick, PoseStack matrixStack, MultiBufferSource buffers, int light) {
        MiscData miscData = MiscData.get(entity);
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            ChunkSystemLevel chunkSystemLevel = (ChunkSystemLevel)world;
            EntityLookup storage = chunkSystemLevel.moonrise$getEntityLookup();
            Objects.requireNonNull(storage);

            miscData.checkScepterTarget(storage::get);

            for(UUID target : miscData.getTargetedByScepters()) {
                CockatriceBeamRenderer.render(entity, storage.get(target), matrixStack, buffers, partialRenderTick);
            }

            MobEffectInstance effect = entity.getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((MobEffect) IafStatusEffects.FROZEN.get()));
            if (effect != null) {
                FrozenStateRenderer.render(entity, matrixStack, buffers, light, effect.getDuration());
            }

            ChainData chainData = ChainData.get(entity);
            ChainRenderer.render(entity, matrixStack, buffers, light, chainData.getChainedTo());
        }
    }
}
