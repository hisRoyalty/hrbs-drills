package com.hisroyalty.hrbsdrills.sound;

import com.hisroyalty.hrbsdrills.DrillsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            DrillsMod.MODID);

    public static RegistryObject<SoundEvent> DRILL = SOUNDS.register("drill",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DrillsMod.MODID, "drill")));
}