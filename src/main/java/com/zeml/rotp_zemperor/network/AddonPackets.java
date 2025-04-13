package com.zeml.rotp_zemperor.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.network.server.BulletTrailPacket;
import com.zeml.rotp_zemperor.network.server.TrBulletTracePacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class AddonPackets {
    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel serverChannel;

    private static SimpleChannel clientChannel;
    private static int packetIndex = 0;

    public static void init(){
        serverChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RotpEmperorAddon.MOD_ID, "server_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();
        clientChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RotpEmperorAddon.MOD_ID, "client_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        packetIndex = 0;

        serverChannel.registerMessage(packetIndex++, BulletTrailPacket.class,BulletTrailPacket::encode,BulletTrailPacket::decode,
                BulletTrailPacket::handle,Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(serverChannel, new TrBulletTracePacket.Handler(),Optional.of(NetworkDirection.PLAY_TO_CLIENT));


    }


    public static void sendToServer(Object msg) {
        clientChannel.sendToServer(msg);
    }

    public static void sendToClient(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            serverChannel.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }


    public static void sendToClientsTracking(Object msg, Entity entity) {
        serverChannel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    private static <MSG> void registerMessage(SimpleChannel channel, IModPacketHandler<MSG> handler, Optional<NetworkDirection> networkDirection) {
        if (packetIndex > 127) {
            throw new IllegalStateException("Too many packets (> 127) registered for a single channel!");
        }
        channel.registerMessage(packetIndex++, handler.getPacketClass(), handler::encode, handler::decode, handler::enqueueHandleSetHandled, networkDirection);
    }

}
