package com.hisroyalty.hrbsdrills.network;

import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SnapPacket {
    public SnapPacket() {}
    public SnapPacket(FriendlyByteBuf buffer) {}
    public void toBytes(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null) {
                Entity entity = sender.getVehicle();
                if (entity instanceof DrillEntity drill) {
                    drill.center();
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
