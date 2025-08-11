package com.zeml.rotp_zemperor.item;

import com.github.standobyte.jojo.entity.itemprojectile.StandArrowEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.zeml.rotp_zemperor.init.InitItems;
import com.zeml.rotp_zemperor.init.InitSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.stream.Stream;

import static net.minecraft.world.GameType.SPECTATOR;

public class EmperorItem extends Item {
    private float mulStamina = 1;
    private float damage =6;
    private float speed=12;

    public EmperorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(World world, LivingEntity entity, ItemStack stack, int remainingTicks) {
        if(entity != null && entity.isAlive()){
            IStandPower.getStandPowerOptional(entity).ifPresent(standPower -> {
                this.mulStamina = entity.hasEffect(ModStatusEffects.RESOLVE.get())?.5F:1F;
                this.damage =  entity.hasEffect(ModStatusEffects.RESOLVE.get())?(float)standPower.getType().getStats().getBasePower() :(float) standPower.getType().getStats().getBasePower()/2;
                this.speed = (float) standPower.getType().getStats().getBaseAttackSpeed()/6;
                if(entity.isShiftKeyDown()){
                    int t = remainingTicks%13;
                    boolean shotTick = t==1|| t==3||t==5|| t==7||t==9 || t==11;

                    if(!world.isClientSide()){

                        if(shotTick && standPower.getStamina()>=35*this.mulStamina){
                            EmperorBullet bullet = new EmperorBullet(entity, world);
                            bullet.shootFromRotation(entity, this.speed, 0);
                            bullet.setDamage(this.damage);

                            CompoundNBT nbt = stack.getOrCreateTag();
                            setTargetToBullet(bullet,entity,nbt);

                            if(standPower.getStandManifestation() instanceof StandEntity){
                                EmperorEntity emperorEntity = (EmperorEntity) standPower.getStandManifestation();
                                if(emperorEntity.getTarget().isPresent()){
                                    bullet.setTarget((LivingEntity) ((ServerWorld) world).getEntity(emperorEntity.getTarget().get()));
                                }
                            }

                            world.addFreshEntity(bullet);
                            standPower.consumeStamina(35*mulStamina);
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
                        if(shotTick && standPower.getStamina() >=40*mulStamina){
                            EmperorBullet bullet = new EmperorBullet(entity, world);
                            bullet.shootFromRotation(entity, this.speed, 0);

                            CompoundNBT nbt = stack.getOrCreateTag();
                            setTargetToBullet(bullet,entity,nbt);

                            if(standPower.getStandManifestation() instanceof StandEntity){
                                EmperorEntity emperorEntity = (EmperorEntity) standPower.getStandManifestation();
                                if(emperorEntity.getTarget().isPresent()){
                                    bullet.setTarget((LivingEntity) ((ServerWorld) world).getEntity(emperorEntity.getTarget().get()));
                                }
                            }

                            world.addFreshEntity(bullet);
                            standPower.consumeStamina(40*this.mulStamina);

                            world.playSound(null, entity.blockPosition(), InitSounds.EMP_SHOT.get(), SoundCategory.PLAYERS, 1, 1);
                            entity.releaseUsingItem();
                        }
                    }
                }
            });
        }

    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(handStack);

    }


    public static void setTargetToBullet(EmperorBullet bullet, LivingEntity entity ,CompoundNBT nbt){
        switch (Math.abs(nbt.getInt("mode")%4)){
            case 1:
                if(targetsHostile(entity).findFirst().isPresent()){
                    bullet.setTarget(getTarget(targetsHostile(entity),entity).get());
                }
                break;
            case 2:
                if(targetsPlayers(entity).findFirst().isPresent()){
                    bullet.setTarget(getTarget(targetsPlayers(entity),entity).get());
                }
                break;
            case 3:
                if (targets(entity).findAny().isPresent()) {
                    bullet.setTarget(getTarget(targets(entity), entity).get());
                }
        }
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
                .filter(livingEntity -> !(livingEntity instanceof PlayerEntity && MCUtil.getGameMode((PlayerEntity) livingEntity) == SPECTATOR))

                ;
    }

    public static Stream<LivingEntity> targetsHostile(LivingEntity player) {

        return player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(19.4,1,19.4),EntityPredicates.ENTITY_STILL_ALIVE).stream()
                .filter(livingEntity -> livingEntity != player)
                .filter(livingEntity -> !(livingEntity instanceof EmperorEntity))
                .filter(livingEntity -> !livingEntity.isAlliedTo(player))
                .filter(livingEntity -> livingEntity instanceof MonsterEntity)
                ;
    }

    public static Stream<LivingEntity> targetsPlayers(LivingEntity player) {

        return player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(19.4,1,19.4),EntityPredicates.ENTITY_STILL_ALIVE).stream()
                .filter(livingEntity -> livingEntity != player)
                .filter(livingEntity -> livingEntity instanceof PlayerEntity || livingEntity instanceof StandEntity)
                .filter(livingEntity -> !(livingEntity instanceof EmperorEntity))
                .filter(livingEntity -> !livingEntity.isAlliedTo(player))
                .filter(livingEntity -> !(livingEntity instanceof PlayerEntity && MCUtil.getGameMode((PlayerEntity) livingEntity) == SPECTATOR))
                ;
    }

}
