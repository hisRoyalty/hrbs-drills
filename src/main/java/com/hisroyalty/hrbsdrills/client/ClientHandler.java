package com.hisroyalty.hrbsdrills.client;

import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.hisroyalty.hrbsdrills.network.DrillNetworking;
import com.hisroyalty.hrbsdrills.network.OpenDrillFuelMenuPacket;
import com.hisroyalty.hrbsdrills.network.SnapPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

@Mod.EventBusSubscriber(Dist.CLIENT)

public class ClientHandler {

    public static KeyMapping openInv;
    public static KeyMapping snap;
    public static String keyCategoryDrill = "key.categories.hrbsdrills";



    static {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ClientHandler::registerKeys);
    }


    public static void registerKeys(RegisterKeyMappingsEvent e) {
        openInv = new KeyMapping("key.hrbsdrills.openInv", GLFW.GLFW_KEY_M , keyCategoryDrill);
        snap = new KeyMapping("key.hrbsdrills.snap", GLFW.GLFW_KEY_SPACE, keyCategoryDrill);
        e.register(openInv);
        e.register(snap);
    }



    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent e) {
        final Player player = e.player;
        if (player instanceof LocalPlayer) {
            if (player.isPassenger() && player.getVehicle() instanceof DrillEntity drill) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.screen == null && mc.getOverlay() == null && openInv.consumeClick()) {
                    DrillNetworking.DRILL.sendToServer(new OpenDrillFuelMenuPacket());
                }
                if (mc.screen == null && mc.getOverlay() == null && snap.consumeClick()) {
                    DrillNetworking.DRILL.sendToServer(new SnapPacket());
                    drill.center();

                }


            }
        }
    }






}
