package com.zeml.rotp_zhp.action.stand.projectile;

import com.github.standobyte.jojo.action.stand.StandEntityAction;

public class GiveEmperor extends StandEntityAction {

    public GiveEmperor(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public boolean enabledInHudDefault() {
        return false;
    }

}
