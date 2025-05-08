package com.zeml.rotp_zemperor.actions;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import net.minecraft.world.World;

public class RemoveStandTarget extends StandEntityAction {

    public RemoveStandTarget(StandEntityAction.Builder builder){
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if(!world.isClientSide){
            ((EmperorEntity) standEntity).setNotTarget();
        }
    }
}
