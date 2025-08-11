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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class ShootEntity extends StandEntityAction {

    public ShootEntity(StandEntityAction.Builder builder){
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
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if(!world.isClientSide && userPower.getUser() != null){
            EmperorBullet bullet = new EmperorBullet(userPower.getUser(), world);
            bullet.shootFromRotation(userPower.getUser(), (float) standEntity.getAttackSpeed()/6, 0);
            bullet.setDamage((float) standEntity.getAttackDamage());
            ItemStack itemStack = getEmperor(userPower.getUser());
            if(!itemStack.isEmpty()){
                CompoundNBT nbt = itemStack.getOrCreateTag();
                EmperorItem.setTargetToBullet(bullet, userPower.getUser(), nbt);
                EmperorEntity emperorEntity = (EmperorEntity)standEntity;
                if(emperorEntity.getTarget().isPresent()){
                    bullet.setTarget((LivingEntity) ((ServerWorld) world).getEntity(emperorEntity.getTarget().get()));
                }
                world.addFreshEntity(bullet);
                world.playSound(null, userPower.getUser().blockPosition(), InitSounds.EMP_SHOT.get(), SoundCategory.PLAYERS, 1, 1);

            }
        }

    }

    @Override
    protected void onTaskStopped(World world, StandEntity standEntity, IStandPower standPower, StandEntityTask task, @Nullable StandEntityAction newAction) {
        super.onTaskStopped(world, standEntity, standPower, task, newAction);
        if(!world.isClientSide){
            standPower.setCooldownTimer(InitStands.SHOOT_BARRAGE_ENTITY.get(), 10);
        }
    }

    public static ItemStack getEmperor(LivingEntity user){
        if(user.getMainHandItem().getItem() instanceof EmperorItem){
            return user.getMainHandItem();
        } else if (user.getOffhandItem().getItem() instanceof EmperorItem) {
            return user.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }


    @Override
    public float getStaminaCost(IStandPower stand) {
        if(stand.getUser() != null && stand.getUser().hasEffect(ModStatusEffects.RESOLVE.get())){
            return .5F*super.getStaminaCost(stand);
        }
        return super.getStaminaCost(stand);
    }

    @Override
    public boolean isLegalInHud(IStandPower power) {
        if(power.getUser() instanceof PlayerEntity){
            return false;
        }
        return super.isLegalInHud(power);
    }
}
