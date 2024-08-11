package com.hisroyalty.hrbsdrills.mixin;


import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenEffectRenderer.class)
public class GuiMixin {
    @Inject(method = "renderFire", at = @At(value = "HEAD"), cancellable = true)
    private static void removeFireOverlay(Minecraft pMinecraft, PoseStack pPoseStack, CallbackInfo ci) {
        LocalPlayer player = pMinecraft.player;
        if (player != null && player.getControlledVehicle() instanceof DrillEntity drill && drill.getNetherite()) {
            ci.cancel();
        }

    }

}
