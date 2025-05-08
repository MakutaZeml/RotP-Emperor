package com.zeml.rotp_zemperor.entity.stand.stands;


import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.zeml.rotp_zemperor.client.playeranim.anim.AddonPlayerAnimations;
import com.zeml.rotp_zemperor.network.AddonPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class EmperorEntity extends StandEntity {
    private static final DataParameter<Byte> TIMER = EntityDataManager.defineId(EmperorEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> SUMON = EntityDataManager.defineId(EmperorEntity.class,DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.defineId(EmperorEntity.class,DataSerializers.OPTIONAL_UUID);


    public EmperorEntity(StandEntityType<EmperorEntity> type, World world){
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    @Override
    public void tick() {
        super.tick();
        if(entityData.get(TIMER) < 20 && entityData.get(SUMON)){
            entityData.set(TIMER,(byte)(entityData.get(TIMER)+1));
        }
        if(entityData.get(TIMER) == 20 && entityData.get(SUMON)){
            if(this.getUser() instanceof PlayerEntity){
                if(this.level.isClientSide){
                    AddonPlayerAnimations.summon_emp.stopAnim((PlayerEntity) this.getUser());
                }else {
                    entityData.set(SUMON,false);
                    entityData.set(TIMER,(byte)0);
                }
            }
        }
        if(!level.isClientSide){
            if(getTarget().isPresent()){
                Entity entity = ((ServerWorld) this.level).getEntity(this.getTarget().get());
                if(entity != null && !entity.isAlive()){
                    this.setNotTarget();
                }
            }
        }
    }

    private final StandRelativeOffset offsetDefault = StandRelativeOffset.withYOffset(0, 0, 0);

    @Override
    public boolean isPickable(){ return false;}

	public StandRelativeOffset getDefaultOffsetFromUser() {return offsetDefault;}


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TIMER,(byte)0);
        entityData.define(SUMON,true);
        entityData.define(TARGET,Optional.empty());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(this.level.isClientSide && this.getUser() instanceof PlayerEntity){
            AddonPlayerAnimations.summon_emp.setWindupAnim((PlayerEntity) this.getUser());
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if(this.level.isClientSide && this.getUser() instanceof PlayerEntity){
            AddonPlayerAnimations.summon_emp.stopAnim((PlayerEntity) this.getUser());
        }
    }

    public Optional<UUID> getTarget(){
        return this.entityData.get(TARGET);
    }

    public void setTarget(UUID uuid){
        this.entityData.set(TARGET,Optional.of(uuid));
    }

    public void setNotTarget(){
        this.entityData.set(TARGET, Optional.empty());
    }

}
