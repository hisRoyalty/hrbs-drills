package com.hisroyalty.hrbsdrills.upgrade.model;

import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class Stowage extends GeoRenderLayer<DrillEntity> {


    public Stowage(GeoRenderer<DrillEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public GeoModel<DrillEntity> getGeoModel() {
        return super.getGeoModel();
    }

    @Override
    public void render(PoseStack poseStack, DrillEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        StowageModel model = new StowageModel();
        ResourceLocation modelResource = model.getModelResource(animatable);
        RenderType renderType1 = RenderType.entityCutout(model.getTextureResource(animatable));

        float yaw = -animatable.getYRot();
        float pitch = animatable.getXRot();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        getRenderer().reRender(model.getBakedModel(modelResource), poseStack, bufferSource, animatable, renderType1, bufferSource.getBuffer(renderType1), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poseStack.popPose();

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }



}
