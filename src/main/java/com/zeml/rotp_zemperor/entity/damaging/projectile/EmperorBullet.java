package com.zeml.rotp_zemperor.entity.damaging.projectile;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.entity.damaging.projectile.ModdedProjectileEntity;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EmperorBullet extends ModdedProjectileEntity {
    private Optional<LivingEntity> homingTarget;
    LivingEntity shooter;
    private float damage=4;
    public final List<Vector3d> tracePos = new LinkedList<>();
    public final List<Double> traceX = new LinkedList<>();
    public final List<Double> traceY = new LinkedList<>();
    public final List<Double> traceZ = new LinkedList<>();

    public Vector3d initialPos;

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
                        setDeltaMovement(vecToTarget.normalize().scale(this.getDeltaMovement().length()));
                    }

            });
        }

    }

    @Override
    public float getBaseDamage() {
        if(shooter != null){
            if(distanceTo(shooter)<=12){
                return damage*JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get().floatValue();
            }
            return (damage+6-distanceTo(shooter)/2)*JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get().floatValue();

        }
        return 6;
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
        if (this.shooter != null && this.distanceTo(this.shooter) > 30) {
            this.remove();
        }
        if(level.isClientSide){
            Vector3d pos = this.position();


            boolean addPos = true;
            if (this.tracePos.size() > 1) {
                Vector3d lastPos = this.tracePos.get(this.tracePos.size() - 1);
                addPos &= pos.distanceToSqr(lastPos) >= 0.0005;
            }
            if (addPos) {
                this.tracePos.add(pos);
            }

            System.out.println(tracePos);

            /*
            boolean addingPos = true;
            if (this.traceX.size() >1 && this.traceY.size() >1 && this.traceZ.size() > 1 ){
                Vector3d lastPosi = new Vector3d(this.traceX.get(this.traceX.size()-1),this.traceY.get(this.traceY.size()-1), this.traceZ.get(this.traceZ.size()-1));
                addingPos &= pos.distanceToSqr(lastPosi) >= 0.0625;
            }
            if(addingPos){
                this.traceX.add(pos.x);
                this.traceY.add(pos.y);
                this.traceZ.add(pos.z);

                AddonPackets.sendToClientsTracking(new BulletXPacket(this.getId(), pos.x), this);
                AddonPackets.sendToClientsTracking(new BulletYPacket(this.getId(), pos.y), this);
                AddonPackets.sendToClientsTracking(new BulletZPacket(this.getId(), pos.z), this);
            }
             */
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
