package com.zeml.rotp_zemperor.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BulletYPacket {
    private final int entityID;
    private final double posX;

    public BulletYPacket(int entityID, double posX){
        this.entityID = entityID;
        this.posX = posX;
    }

    public static void encode(BulletYPacket msg, PacketBuffer buf){
        buf.writeInt(msg.entityID);
        buf.writeDouble(msg.posX);
    }

    public static BulletYPacket decode(PacketBuffer buf){
        return new BulletYPacket(buf.readInt(),buf.readDouble());
    }

    public static void handle(BulletYPacket msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityID);
            if (entity instanceof EmperorBullet) {
                EmperorBullet bullet = (EmperorBullet) entity;
                bullet.traceY.add(msg.posX);
            }
        });
        ctx.get().setPacketHandled(true);

    }

}
