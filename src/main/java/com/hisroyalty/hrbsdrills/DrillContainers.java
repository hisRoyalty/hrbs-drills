package com.hisroyalty.hrbsdrills;

import com.hisroyalty.hrbsdrills.container.DrillContainer;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DrillContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DrillsMod.MODID);

    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MenuType<DrillContainer>> DRILL_CONTAINER = CONTAINERS.register("drill_container", () -> IForgeMenuType.create(DrillContainer::new));


}
