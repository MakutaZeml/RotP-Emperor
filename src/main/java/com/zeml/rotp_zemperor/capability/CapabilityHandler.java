package com.zeml.rotp_zemperor.capability;

import com.zeml.rotp_zemperor.RotpEmperorAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RotpEmperorAddon.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation CAPABILITY_ID = new ResourceLocation(RotpEmperorAddon.MOD_ID, "emperor");


    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(
                LivingData.class,
                new Capability.IStorage<LivingData>() {
                    @Override public INBT writeNBT(Capability<LivingData> capability, LivingData instance, Direction side) { return instance.serializeNBT(); }
                    @Override public void readNBT(Capability<LivingData> capability, LivingData instance, Direction side, INBT nbt) { instance.deserializeNBT((CompoundNBT) nbt); }
                },
                () -> new LivingData(null));
    }

    // Attaches the capability to all instances of LivingEntity.


    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            event.addCapability(CAPABILITY_ID, new LivingDataProvider(living));
        }
    }


    // Event handlers to properly sync the attached data from server to client(s).
    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        Entity entityTracked = event.getTarget();
        ServerPlayerEntity trackingPlayer = (ServerPlayerEntity) event.getPlayer();
        if (entityTracked instanceof LivingEntity) {
            entityTracked.getCapability(LivingDataProvider.CAPABILITY).ifPresent(livingData -> {
                livingData.syncWithAnyPlayer(trackingPlayer);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(LivingDataProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
            data.syncWithAnyPlayer(serverPlayer);
        });
    }
}
