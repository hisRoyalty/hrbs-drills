package com.hisroyalty.hrbsdrills.entity.render;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;

public class DrillEntityModel extends GeoModel<DrillEntity> {
    public ResourceLocation getModelResource(DrillEntity object) {
        if (object.getNetherite()) {
            return new ResourceLocation(DrillsMod.MODID, "geo/netherite1.geo.json");
        }
        return new ResourceLocation(DrillsMod.MODID, "geo/drill.geo_1.json");
    }

    public ResourceLocation getTextureResource(DrillEntity object) {
        if (object.getNetherite()) {
            return new ResourceLocation(DrillsMod.MODID, "textures/entity/netherite1.png");
        }
        return new ResourceLocation(DrillsMod.MODID, "textures/entity/drill.png");
    }

    public ResourceLocation getAnimationResource(DrillEntity animatable) {
        if (animatable.getNetherite()) {
            return new ResourceLocation(DrillsMod.MODID, "animations/netherite.json");
        }
        return new ResourceLocation(DrillsMod.MODID, "animations/drill.json");
    }


    @Override
    public void setCustomAnimations(DrillEntity animatable, long instanceId, software.bernie.geckolib.core.animation.AnimationState<DrillEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone main = this.getAnimationProcessor().getBone("bone3");
        if (animatable.hasControllingPassenger()) {
            if (animatable.isVehicle()) {
                main.setRotY((float) -Math.toRadians(animatable.getYRot()));
            }
        } else {
            main.setRotY((float) -Math.toRadians(animatable.getYRot()));
        }
    }
}
