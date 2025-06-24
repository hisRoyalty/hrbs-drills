package com.hisroyalty.hrbsdrills.entity.render;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;

public class DrillEntityModel extends GeoModel<DrillEntity> {
    public ResourceLocation getModelResource(DrillEntity object) {
        return new ResourceLocation(DrillsMod.MODID, "geo/drill_base.json");
    }

    public ResourceLocation getTextureResource(DrillEntity object) {
        if (object.getNetherite()) {
            return new ResourceLocation(DrillsMod.MODID, "textures/entity/netherite_new.png");
        }
        return new ResourceLocation(DrillsMod.MODID, "textures/entity/drill_base.png");
    }

    public ResourceLocation getAnimationResource(DrillEntity animatable) {
        return new ResourceLocation(DrillsMod.MODID, "animations/drill2.json");
    }


    @Override
    public void setCustomAnimations(DrillEntity animatable, long instanceId, software.bernie.geckolib.core.animation.AnimationState<DrillEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone main = this.getAnimationProcessor().getBone("main");
        CoreGeoBone sawDrill = this.getAnimationProcessor().getBone("sawdrill");
        CoreGeoBone drill = this.getAnimationProcessor().getBone("drill");

        if (animatable.hasControllingPassenger()) {
            if (animatable.isVehicle()) {
                main.setRotY((float) -Math.toRadians(animatable.getYRot()));
            }
        } else {
            main.setRotY((float) -Math.toRadians(animatable.getYRot()));
        }


        if (animatable.hasDrillHead()) {
            sawDrill.setHidden(true);
            drill.setHidden(false);
        } else if (animatable.hasSawDrillHead()) {
            sawDrill.setHidden(false);
            drill.setHidden(true);
        } else {
            sawDrill.setHidden(true);
            drill.setHidden(true);
        }
    }


}
