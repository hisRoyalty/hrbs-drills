package com.hisroyalty.hrbsdrills.entity;

import com.hisroyalty.hrbsdrills.DrillsMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = DrillsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DrillsMod.MODID);




    public static final RegistryObject<EntityType<DrillEntity>> DRILL = ENTITY_TYPES.register("drill",
            () -> EntityType.Builder.<DrillEntity>of(DrillEntity::new, MobCategory.MISC).sized(2.5F, 3.5F)
                    .clientTrackingRange(12).updateInterval(1).build("drill"));




    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
