package com.zeml.rotp_zemperor.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BulletZPacket {
    private final int entityID;
    private final double posX;

    public BulletZPacket(int entityID, double posX){
        this.entityID = entityID;
        this.posX = posX;
    }

    public static void encode(BulletZPacket msg, PacketBuffer buf){
        buf.writeInt(msg.entityID);
        buf.writeDouble(msg.posX);
    }

    public static BulletZPacket decode(PacketBuffer buf){
        return new BulletZPacket(buf.readInt(),buf.readDouble());
    }

    public static void handle(BulletZPacket msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityID);
            if (entity instanceof EmperorBullet) {
                EmperorBullet bullet = (EmperorBullet) entity;
                bullet.traceX.add(msg.posX);
            }
        });
        ctx.get().setPacketHandled(true);

    }

}
