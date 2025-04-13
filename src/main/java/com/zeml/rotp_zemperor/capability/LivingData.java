package com.zeml.rotp_zemperor.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class LivingData implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    private int mode = 0;

    public LivingData(LivingEntity entity) {
        this.entity = entity;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {

        //AddonPackets.sendToClient(new TrPickaxesThrownPacket(entity.getId(), pickaxesThrown), player);
    }

    // If there is data that should only be known to the player, and not to other ones, sync it here instead.
    public void syncWithEntityOnly(ServerPlayerEntity player) {
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("mode",this.mode);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.mode = nbt.getInt("mode");
    }


}
