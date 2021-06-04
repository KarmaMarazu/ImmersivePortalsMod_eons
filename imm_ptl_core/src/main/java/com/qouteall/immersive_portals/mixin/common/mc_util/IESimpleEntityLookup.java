package com.qouteall.immersive_portals.mixin.common.mc_util;

import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleEntityLookup.class)
public interface IESimpleEntityLookup {
    @Accessor
    SectionedEntityCache<?> getCache();
}
