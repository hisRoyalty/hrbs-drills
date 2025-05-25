package com.hisroyalty.hrbsdrills.entity.render;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.hisroyalty.hrbsdrills.upgrade.model.DrillModel;
import com.hisroyalty.hrbsdrills.upgrade.model.SawDrillModel;
import com.hisroyalty.hrbsdrills.upgrade.model.Stowage;
import com.hisroyalty.hrbsdrills.upgrade.model.StowageModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrillEntityRenderer extends GeoEntityRenderer<DrillEntity> {
    public DrillEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DrillEntityModel());
        this.shadowRadius = 0.7F;

    }

    @Override
    public float getMotionAnimThreshold(DrillEntity animatable) {
        return 0.03f;
    }

    @Override
    public void render(DrillEntity entity, float pEntityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.getHasChestUpgrade()) {
            StowageModel model = new StowageModel();
            ResourceLocation modelResource = model.getModelResource(entity);
            RenderType renderType1 = RenderType.entityCutout(model.getTextureResource(entity));

            float yaw = -entity.getYRot();
            float pitch = entity.getXRot();

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

            reRender(model.getBakedModel(modelResource), poseStack, bufferSource, entity, renderType1, bufferSource.getBuffer(renderType1), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

            poseStack.popPose();
        }
        ItemStack stack = entity.getItemHandler().getStackInSlot(2);

        if (entity.hasDrillHead()) {
            DrillModel model = new DrillModel();
            ResourceLocation modelResource = model.getModelResource(entity);
            RenderType renderType1 = RenderType.entityCutout(model.getTextureResource(entity));

            float yaw = -entity.getYRot();
            float pitch = entity.getXRot();

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

            reRender(model.getBakedModel(modelResource), poseStack, bufferSource, entity, renderType1, bufferSource.getBuffer(renderType1), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

            poseStack.popPose();
        }
        if (entity.hasSawDrillHead()) {
            SawDrillModel model = new SawDrillModel();
            ResourceLocation modelResource = model.getModelResource(entity);
            RenderType renderType1 = RenderType.entityCutout(model.getTextureResource(entity));

            float yaw = -entity.getYRot();
            float pitch = entity.getXRot();

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

            reRender(model.getBakedModel(modelResource), poseStack, bufferSource, entity, renderType1, bufferSource.getBuffer(renderType1), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

            poseStack.popPose();

        }



        super.render(entity, pEntityYaw, partialTick, poseStack, bufferSource, packedLight);


    }

}
