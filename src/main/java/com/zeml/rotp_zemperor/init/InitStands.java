package com.zeml.rotp_zemperor.init;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.actions.GiveEmperor;
import com.zeml.rotp_zemperor.actions.RemoveStandTarget;
import com.zeml.rotp_zemperor.actions.StandTarget;
import com.zeml.rotp_zemperor.actions.TargetSelected;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpEmperorAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpEmperorAddon.MOD_ID);

    // ======================================== The Emperor ========================================


    public static final RegistryObject<StandEntityAction> EMP_GIVE= ACTIONS.register("give_emp",
            ()->new GiveEmperor(new StandEntityAction.Builder().staminaCost(1)));


    public static final RegistryObject<StandEntityAction> TRAGET= ACTIONS.register("target",
            ()->new TargetSelected(new StandEntityAction.Builder().staminaCost(1).resolveLevelToUnlock(1)));

    public static final RegistryObject<StandEntityAction> STAND_TARGET = ACTIONS.register("stand_target",
            ()-> new StandTarget(new StandEntityAction.Builder().resolveLevelToUnlock(3)));

    public static final RegistryObject<StandEntityAction> NO_STAND_TARGET = ACTIONS.register("no_stand_target",
            ()-> new RemoveStandTarget(new StandEntityAction.Builder())
            );

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<EmperorEntity>> STAND_EMPEROR =
            new EntityStandRegistryObject<>("the_emperor",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xC7DDE0)
                            .storyPartName(ModStandsInit.PART_3_NAME)
                            .leftClickHotbar(
                                    TRAGET.get(),
                                    STAND_TARGET.get()

                            )
                            .rightClickHotbar(
                                    EMP_GIVE.get()

                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .tier(3)
                                    .power(12.0)
                                    .speed(12.0)
                                    .range(12.0)
                                    .durability(8.0)
                                    .precision(2.0)
                                    .randomWeight(1)
                            )
                            .addOst(InitSounds.HOL_OSST)
                            .disableManualControl().disableStandLeap()
                            .addSummonShout(InitSounds.USER_EMPEROR)
                            .build(),

                    InitEntities.ENTITIES,
                    () -> new StandEntityType<EmperorEntity>(EmperorEntity::new, 0F, 0F)
                            .summonSound(InitSounds.EMPEROR_SUMMON)
                            .unsummonSound(InitSounds.EMPEROR_UNSUMMON))
                    .withDefaultStandAttributes();
}
