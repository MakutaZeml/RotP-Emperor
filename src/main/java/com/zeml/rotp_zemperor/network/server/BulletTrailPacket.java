package com.zeml.rotp_zemperor.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BulletTrailPacket {
    private final int entityID;

    public BulletTrailPacket(int entityID){
        this.entityID = entityID;
    }


    public static void encode(BulletTrailPacket msg, PacketBuffer buf){
        buf.writeInt(msg.entityID);
    }


    public static BulletTrailPacket decode(PacketBuffer buffer){
        return new BulletTrailPacket(buffer.readInt());
    }

    public static void handle(BulletTrailPacket msg, Supplier<NetworkEvent.Context> ctx){
        Entity entity = ClientUtil.getEntityById(msg.entityID);
        if(entity instanceof EmperorBullet){
            EmperorBullet bullet = (EmperorBullet) entity;
            Vector3d pos = bullet.position();
            boolean addPos = true;
            if (bullet.tracePos.size() > 1) {
                Vector3d lastPos = bullet.tracePos.get(bullet.tracePos.size() - 1);
                addPos &= pos.distanceToSqr(lastPos) >= 0.0005;
            }
            if (addPos) {
                bullet.tracePos.add(pos);
            }
        }
    }
}
