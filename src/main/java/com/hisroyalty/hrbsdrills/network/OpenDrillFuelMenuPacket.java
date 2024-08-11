package com.hisroyalty.hrbsdrills.network;

import com.hisroyalty.hrbsdrills.container.DrillContainer;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenDrillFuelMenuPacket {
    public OpenDrillFuelMenuPacket() {}
    public OpenDrillFuelMenuPacket(FriendlyByteBuf buffer) {}
    public void toBytes(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null) {
                Entity entity = sender.getVehicle();
                if (entity instanceof DrillEntity drill && !drill.level().isClientSide) {

              NetworkHooks.openScreen((sender), (DrillEntity)entity, buffer -> buffer.writeVarInt(drill.getId()));
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
