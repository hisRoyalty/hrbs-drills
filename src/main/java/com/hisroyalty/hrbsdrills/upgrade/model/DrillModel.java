package com.hisroyalty.hrbsdrills.upgrade.model;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class DrillModel extends GeoModel<DrillEntity> {
    @Override
    public ResourceLocation getModelResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "geo/drill_drill.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "textures/entity/drill_drill.png");

    }

    @Override
    public ResourceLocation getAnimationResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "animations/drill_drill.json");
    }
}
