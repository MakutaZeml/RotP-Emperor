package com.zeml.rotp_zemperor.actions;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.zeml.rotp_zemperor.init.InitStands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StandTarget extends StandEntityAction {

    public StandTarget(StandEntityAction.Builder builder){
        super(builder);
    }


    @Override
    protected Action<IStandPower> replaceAction(IStandPower power, ActionTarget target) {
        if(power.getStandManifestation() instanceof StandEntity){
            EmperorEntity emperorEntity = ((EmperorEntity) power.getStandManifestation());
            if(emperorEntity.getTarget().isPresent()){
                return InitStands.NO_STAND_TARGET.get();
            }
        }
        return this;
    }

    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if(power.getUser() != null){
            RayTraceResult rayTrace =JojoModUtil.rayTrace(power.getUser(),stand.getMaxRange(), entity -> entity.isAlive() && !entity.isAlliedTo(power.getUser()),2);
            if(rayTrace.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult) rayTrace).getEntity() instanceof LivingEntity){
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if(!world.isClientSide){
            RayTraceResult rayTrace =JojoModUtil.rayTrace(userPower.getUser(),standEntity.getMaxRange(), entity -> entity.isAlive() && !entity.isAlliedTo(userPower.getUser()),2);
            if(rayTrace.getType() == RayTraceResult.Type.ENTITY){
                Entity entity = ((EntityRayTraceResult) rayTrace).getEntity();
                if(entity instanceof LivingEntity){
                    ((EmperorEntity) standEntity).setTarget(entity.getUUID());
                }
            }

        }
    }


    @Override
    public StandAction[] getExtraUnlockable() {
        return new StandAction[]{InitStands.NO_STAND_TARGET.get()};
    }
}
