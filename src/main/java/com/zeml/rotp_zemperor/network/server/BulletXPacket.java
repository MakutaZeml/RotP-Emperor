package com.zeml.rotp_zemperor.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BulletXPacket {
    private final int entityID;
    private final double posX;

    public BulletXPacket(int entityID, double posX){
        this.entityID = entityID;
        this.posX = posX;
    }

    public static void encode(BulletXPacket msg, PacketBuffer buf){
        buf.writeInt(msg.entityID);
        buf.writeDouble(msg.posX);
    }

    public static BulletXPacket decode(PacketBuffer buf){
        return new BulletXPacket(buf.readInt(),buf.readDouble());
    }

    public static void handle(BulletXPacket msg, Supplier<NetworkEvent.Context> ctx){
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
