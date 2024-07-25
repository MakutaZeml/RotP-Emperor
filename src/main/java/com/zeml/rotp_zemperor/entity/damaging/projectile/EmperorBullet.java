package com.zeml.rotp_zemperor.entity.damaging.projectile;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.entity.damaging.projectile.ModdedProjectileEntity;
import com.github.standobyte.jojo.init.ModParticles;
import com.zeml.rotp_zemperor.init.InitEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EmperorBullet extends ModdedProjectileEntity {
    private Optional<LivingEntity> homingTarget;
    LivingEntity shooter;
    private float damage=4;

    public EmperorBullet(LivingEntity shooter, World world) {
        super(InitEntities.EMPEROR_BULLET.get(), shooter, world);
        this.shooter = shooter;
    }

    public EmperorBullet(EntityType<EmperorBullet> emperorBulletEntityType, World world) {
        super(emperorBulletEntityType, world);
    }

    public void setTarget(LivingEntity target) {
        this.homingTarget = Optional.ofNullable(target);
    }

    @Override
    public int ticksLifespan() {
        return 100;
    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    @Override
    protected void moveProjectile(){
        super.moveProjectile();

        if(homingTarget != null){

            homingTarget.ifPresent(target -> {

                    Vector3d targetPos = target.getBoundingBox().getCenter();
                    Vector3d vecToTarget = targetPos.subtract(this.position());
                    setDeltaMovement(vecToTarget.normalize().scale(this.getDeltaMovement().length()));
                    if (level.isClientSide()) {
                        target.getBoundingBox().clip(position(), targetPos).ifPresent(pos -> {
                            level.addParticle(ModParticles.CD_RESTORATION.get(),
                                    pos.x + (random.nextDouble() - 0.5) * 0.25,
                                    pos.y + (random.nextDouble() - 0.5) * 0.25,
                                    pos.z + (random.nextDouble() - 0.5) * 0.25,
                                    0, 0, 0);
                        });
                    }

            });
        }

    }

    @Override
    public float getBaseDamage() {
        if(distanceTo(shooter)<=12){
            return damage*JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get().floatValue();
        }
        return (damage+6-distanceTo(shooter)/2)*JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get().floatValue();
    }

    @Override
    protected float getMaxHardnessBreakable() {
        return 0.3F;
    }

    @Override
    public boolean standDamage() {
        return true;
    }

    private UUID targetUUID;
    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        homingTarget.ifPresent(target -> {
            nbt.putUUID("HomingTarget", target.getUUID());
        });
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
        if (this.shooter != null && this.distanceTo(shooter) > 30) {
            this.remove();
        }
    }

    @Override
    protected double getGravityAcceleration() {
        return 0;
    }



    @Override
    protected void afterBlockHit(BlockRayTraceResult blockRayTraceResult, boolean blockDestroyed) {
        if (blockDestroyed) {
            if (!level.isClientSide()) {
                setDeltaMovement(getDeltaMovement().scale(0.9));
                checkHit();
            }
        }
        else {
            BlockPos blockPos = blockRayTraceResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            blockSound(blockPos, blockState);
            this.kill();
        }
    }

    private void blockSound(BlockPos blockPos, BlockState blockState) {
        SoundType soundType = blockState.getSoundType(level, blockPos, this);
        level.playSound(null, blockPos, soundType.getHitSound(), SoundCategory.BLOCKS, 1.0F, soundType.getPitch() * 0.5F);
    }

    @Override
    protected void breakProjectile(ActionTarget.TargetType targetType, RayTraceResult hitTarget) {
        if (targetType != ActionTarget.TargetType.BLOCK) {
            super.breakProjectile(targetType, hitTarget);
        }
    }



}
