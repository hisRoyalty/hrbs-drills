package com.hisroyalty.hrbsdrills.mixin;


import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "setupFog", at = @At(value = "TAIL"))
    private static void setupFog(Camera pCamera, FogRenderer.FogMode pFogMode, float pFarPlaneDistance, boolean p_234176_, float p_234177_, CallbackInfo ci) {
        FogType fogtype = pCamera.getFluidInCamera();
        Entity entity = pCamera.getEntity();

        if (entity instanceof Player player) {
            if (player.getControlledVehicle() instanceof DrillEntity drill)
                if (drill.getNetherite()) {
                    if (fogtype == FogType.LAVA) {
                        RenderSystem.setShaderFogStart(0);
                        RenderSystem.setShaderFogEnd(150F);
                    }
                }
        }
    }



}
