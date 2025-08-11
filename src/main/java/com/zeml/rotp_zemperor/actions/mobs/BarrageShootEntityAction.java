package com.zeml.rotp_zemperor.actions.mobs;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.zeml.rotp_zemperor.init.InitItems;
import com.zeml.rotp_zemperor.init.InitSounds;
import com.zeml.rotp_zemperor.init.InitStands;
import com.zeml.rotp_zemperor.item.EmperorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class BarrageShootEntityAction extends StandEntityAction {

    public BarrageShootEntityAction(StandEntityAction.Builder builder){
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target){
        if(user.getItemInHand(Hand.MAIN_HAND).getItem() == InitItems.EMPEROR.get() || user.getItemInHand(Hand.OFF_HAND).getItem() ==  InitItems.EMPEROR.get()){
            return ActionConditionResult.POSITIVE;
        }
        return conditionMessage("no_emperor");
    }

    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if(!world.isClientSide){
            boolean shotTick = ticksHeld==1|| ticksHeld==3||ticksHeld==5|| ticksHeld==7||ticksHeld==9 || ticksHeld==11;
            if(shotTick){
                EmperorBullet bullet = new EmperorBullet(user, world);
                float speed = (float) power.getType().getStats().getBaseAttackSpeed()/6;
                float damage =  user.hasEffect(ModStatusEffects.RESOLVE.get())?(float)power.getType().getStats().getBasePower() :(float) power.getType().getStats().getBasePower()/2;

                bullet.shootFromRotation(user, speed, 0);
                bullet.setDamage(damage);

                ItemStack stack = ShootEntity.getEmperor(user);
                CompoundNBT nbt = stack.getOrCreateTag();
                EmperorItem.setTargetToBullet(bullet,user,nbt);

                if(power.getStandManifestation() instanceof StandEntity){
                    EmperorEntity emperorEntity = (EmperorEntity) power.getStandManifestation();
                    if(emperorEntity.getTarget().isPresent()){
                        bullet.setTarget((LivingEntity) ((ServerWorld) world).getEntity(emperorEntity.getTarget().get()));
                    }
                }

                world.addFreshEntity(bullet);
                world.playSound(null, user.blockPosition(), InitSounds.EMP_SHOT.get(), SoundCategory.PLAYERS, 1, 1);
            }
        }
    }

    @Override
    protected void onTaskStopped(World world, StandEntity standEntity, IStandPower standPower, StandEntityTask task, @Nullable StandEntityAction newAction) {
        super.onTaskStopped(world, standEntity, standPower, task, newAction);
        if(!world.isClientSide){
            standPower.setCooldownTimer(InitStands.SHOOT_ENTITY.get(), 65);
        }
    }

    @Override
    public float getStaminaCostTicking(IStandPower stand) {
        if(stand.getUser() != null && stand.getUser().hasEffect(ModStatusEffects.RESOLVE.get())){
            return .5F*super.getStaminaCostTicking(stand);
        }
        return super.getStaminaCostTicking(stand);
    }

    @Override
    public boolean isLegalInHud(IStandPower power) {
        if(power.getUser() instanceof PlayerEntity){
            return false;
        }
        return super.isLegalInHud(power);
    }
}
