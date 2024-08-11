package com.hisroyalty.hrbsdrills.mixin;


import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {


    @Shadow
    Input input;

    @Shadow @Final
    protected Minecraft minecraft;



    @Inject(method = "rideTick", at = @At(value = "TAIL"))
    public void rideTick(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer)(Object)this;
        if (self.getVehicle() instanceof DrillEntity drill) {
            drill.setInput(this.input.left, this.input.right, this.input.up, this.input.down);
        }
    }

}

