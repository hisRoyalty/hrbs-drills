package com.hisroyalty.hrbsdrills.upgrade.model;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SawDrillModel extends GeoModel<DrillEntity> {
    @Override
    public ResourceLocation getModelResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "geo/saw_drill_drill.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "textures/entity/saw_drill_drill.png");

    }

    @Override
    public ResourceLocation getAnimationResource(DrillEntity animatable) {
        return null;
//        return new ResourceLocation(DrillsMod.MODID, "animations/saw_drill_drill.json");
    }
}
