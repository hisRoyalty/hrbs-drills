package com.hisroyalty.hrbsdrills.network;

import com.hisroyalty.hrbsdrills.DrillsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class DrillNetworking {

    public static final String NETWORK_VERSION = "0.0.1";
    private static int channel_id = 0;

    public static final SimpleChannel DRILL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DrillsMod.MODID, "drill"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        DRILL.registerMessage(++channel_id, OpenDrillFuelMenuPacket.class, OpenDrillFuelMenuPacket::toBytes, OpenDrillFuelMenuPacket::new, OpenDrillFuelMenuPacket::handle);
        DRILL.registerMessage(++channel_id, SnapPacket.class, SnapPacket::toBytes, SnapPacket::new, SnapPacket::handle);

        }
    }
