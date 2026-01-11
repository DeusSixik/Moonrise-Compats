package dev.sixik.moonrise_compats.mixin_core;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MoonriseCompatsMixinPlugin implements IMixinConfigPlugin {

    public static List<MixinApplier> MixinAppliers = new ObjectArrayList<>();

    public void create(String modClass, MixinApplier.Param... params) {
        MixinAppliers.add(new MixinApplier(modClass, params));
    }

    @Override
    public void onLoad(String s) {
        create("team.creative.enhancedvisuals.EnhancedVisuals", new MixinApplier.Param(
                "dev.sixik.moonrise_compats.mixin.fixes.enhancedvisuals.EnhancedVisuals$ExplosionMixin",
                "team.creative.enhancedvisuals.mixin.ExplosionMixin"
        ));
        create("", new MixinApplier.Param(
                "dev.sixik.moonrise_compats.mixin.fixes.moonrise.LevelMixin",
                "ca.spottedleaf.moonrise.mixin.chunk_system.LevelMixin"
        ), new MixinApplier.Param(
                "",
                "ca.spottedleaf.moonrise.mixin.chunk_system.ChunkGeneratorMixin"
        ));
        create("com.copycatsplus.copycats.Copycats", new MixinApplier.Param(
                "dev.sixik.moonrise_compats.mixin.fixes.copycat.CopyCats$LiquidBlockRendererMixin$Fix",
                "com.copycatsplus.copycats.mixin.foundation.copycat.LiquidBlockRendererMixin"
        ));
        create("com.cupboard.Cupboard", new MixinApplier.Param(
                "",
                "com.cupboard.mixin.ChunkLoadDebug"
        ));
        create("com.teamabnormals.environmental.core.Environmental", new MixinApplier.Param(
                "dev.sixik.moonrise_compats.mixin.fixes.environmental.Environmental$SurfaceSystemMixin",
                "com.teamabnormals.environmental.core.mixin.SurfaceSystemMixin"
        ));
        create("net.mehvahdjukaar.amendments.Amendments",
                new MixinApplier.Param(
                        "dev.sixik.moonrise_compats.mixin.fixes.amendments.Amendments$ExplosionMixin",
                        "net.mehvahdjukaar.amendments.mixins.ExplosionMixin"
                ),
                new MixinApplier.Param(
                        "dev.sixik.moonrise_compats.mixin.fixes.amendments.Amendments$Moonrise$ExplosionMixin",
                        "net.mehvahdjukaar.amendments.mixins.ExplosionMixin"
                )
        );
        create("cn.leolezury.eternalstarlight.common.EternalStarlight",
                new MixinApplier.Param(
                        "dev.sixik.moonrise_compats.mixin.fixes.eternal_starlight.EternalStarlight$Moonrise$EntityMixin",
                        "cn.leolezury.eternalstarlight.common.mixin.EntityMixin"
                )
        );
        create("net.fabricmc.fabric.impl.event.lifecycle.LifecycleEventsImpl",
                new MixinApplier.Param(
                        "dev.sixik.moonrise_compats.mixin.fixes.forgified_fabric_api.FFA$Moonrise$ChunkHolderMixin",
                        "net.fabricmc.fabric.mixin.event.lifecycle.ChunkHolderMixin"
                ));
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        for (MixinApplier mixinApplier : MixinAppliers) {
            if(mixinApplier.hasMixin(mixinClassName) && !mixinApplier.isModLoaded())
                return false;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
