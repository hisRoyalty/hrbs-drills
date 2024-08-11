package com.hisroyalty.hrbsdrills.entity.render;

import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    public void render(DrillEntity entity, float pEntityYaw, float partialTick, PoseStack pMatrixStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, pEntityYaw, partialTick, pMatrixStack, bufferSource, packedLight);

    }
}
