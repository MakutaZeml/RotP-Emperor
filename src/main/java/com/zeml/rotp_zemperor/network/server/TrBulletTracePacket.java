package com.zeml.rotp_zemperor.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import com.zeml.rotp_zemperor.network.AddonPackets;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrBulletTracePacket {
    private final int entityID;
    private final double posX;
    private final double posY;
    private final double posZ;

    public TrBulletTracePacket(int entityID, Vector3d pos){
        this(entityID,pos.x, pos.y, pos.z);
    }

    public TrBulletTracePacket(int entityID, double posX, double posY, double posZ){
        this.entityID = entityID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }


    public static class Handler implements IModPacketHandler<TrBulletTracePacket>{

        @Override
        public void encode(TrBulletTracePacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityID);
            buf.writeDouble(msg.posX);
            buf.writeDouble(msg.posY);
            buf.writeDouble(msg.posZ);
        }

        @Override
        public TrBulletTracePacket decode(PacketBuffer buf) {
            return new TrBulletTracePacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble());
        }

        @Override
        public void handle(TrBulletTracePacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityID);
            if(entity instanceof EmperorBullet){
                EmperorBullet bullet = (EmperorBullet) entity;
                Vector3d pos = new Vector3d(msg.posX, msg.posY, msg.posZ);

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

        @Override
        public Class<TrBulletTracePacket> getPacketClass() {
            return TrBulletTracePacket.class;
        }
    }
}
