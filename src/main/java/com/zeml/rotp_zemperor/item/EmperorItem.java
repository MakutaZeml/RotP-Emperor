package com.zeml.rotp_zemperor.item;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.zeml.rotp_zemperor.init.InitItems;
import com.zeml.rotp_zemperor.init.InitSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public class EmperorItem extends Item {


    public EmperorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(World world, LivingEntity entity, ItemStack stack, int remainingTicks) {

        IStandPower.getStandPowerOptional(entity).ifPresent(standPower -> {
            if(entity.isShiftKeyDown()){

                int t = remainingTicks%13;
                boolean shotTick = t==1|| t==3||t==5|| t==7||t==9 || t==11;

                if(!world.isClientSide()){

                    if(shotTick && standPower.getStamina()>=70){
                        EmperorBullet bullet = new EmperorBullet(entity, world);
                        bullet.shootFromRotation(entity, 12, 0);

                        if (targets(entity).findAny().isPresent()) {
                            bullet.setTarget(getTarget(targets(entity), entity).get());
                        }

                        world.addFreshEntity(bullet);
                        standPower.consumeStamina(70);
                        world.playSound(null, entity.blockPosition(), InitSounds.EMP_SHOT.get(), SoundCategory.PLAYERS, 1, 1);
                    }
                    if(t == 1){
                        entity.releaseUsingItem();
                    }
                }
            }else{

                int t = remainingTicks%5;
                boolean shotTick = t==4;

                if(!world.isClientSide()){
                    if(shotTick && standPower.getStamina() >=80){
                        EmperorBullet bullet = new EmperorBullet(entity, world);
                        bullet.shootFromRotation(entity, 12, 0);

                        if (targets(entity).findAny().isPresent()) {
                            bullet.setTarget(getTarget(targets(entity), entity).get());
                        }

                        world.addFreshEntity(bullet);
                        standPower.consumeStamina(80);

                        world.playSound(null, entity.blockPosition(), InitSounds.EMP_SHOT.get(), SoundCategory.PLAYERS, 1, 1);
                        entity.releaseUsingItem();
                    }
                }
            }
        });


    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(handStack);

    }




    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int remainingTicks) {
        if(entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity;
            if(player.isShiftKeyDown()){
                player.getCooldowns().addCooldown(this,65);
            }else {
                player.getCooldowns().addCooldown(this,10);
            }
        }
    }


    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }


    @Override
    public int getUseDuration(ItemStack stack) {
        return 10;
    }

    public static Optional<LivingEntity> getTarget(Stream<LivingEntity> targets, LivingEntity user) {
        Vector3d lookAngle = user.getLookAngle();
        return targets.max((e1, e2) ->
                MathHelper.floor(
                        (lookAngle.dot(e1.getBoundingBox().getCenter().subtract(user.getEyePosition(1.0F)).normalize()) -
                                lookAngle.dot(e2.getBoundingBox().getCenter().subtract(user.getEyePosition(1.0F)).normalize()))
                                * 256));
    }

    public static Stream<LivingEntity> targets(LivingEntity player) {

        return player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(19.4,1,19.4),EntityPredicates.ENTITY_STILL_ALIVE).stream()
                .filter(livingEntity -> livingEntity != player)
                .filter(livingEntity -> !(livingEntity instanceof EmperorEntity))
                .filter(livingEntity -> !livingEntity.isAlliedTo(player))
                ;
    }



}
