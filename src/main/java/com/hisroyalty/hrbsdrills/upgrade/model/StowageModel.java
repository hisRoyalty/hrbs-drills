package com.hisroyalty.hrbsdrills.upgrade.model;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StowageModel extends GeoModel<DrillEntity> {
    @Override
    public ResourceLocation getModelResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "geo/stowage.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "textures/entity/stowage.png");

    }

    @Override
    public ResourceLocation getAnimationResource(DrillEntity animatable) {
        return null;
    }


}
