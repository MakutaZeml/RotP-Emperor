package com.zeml.rotp_zemperor.actions;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import com.zeml.rotp_zemperor.capability.LivingDataProvider;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.zeml.rotp_zemperor.init.InitItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TargetSelected extends StandEntityAction {
    public TargetSelected(StandEntityAction.Builder builder) {
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
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if(((EmperorEntity) stand).getTarget().isPresent()){
            return ActionConditionResult.NEGATIVE;
        }
        return checkSpecificConditions(power.getUser(),power,target);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            if(userPower.getUser() != null){
                Hand hand = userPower.getUser().getItemInHand(Hand.MAIN_HAND).getItem() == InitItems.EMPEROR.get()? Hand.MAIN_HAND:Hand.OFF_HAND;
                ItemStack itemStack =userPower.getUser().getItemInHand(hand);
                CompoundNBT nbt = itemStack.getOrCreateTag();
                if(!nbt.contains("mode")){
                    nbt.putInt("mode",0);
                }else {
                    int newMode = nbt.getInt("mode")+(userPower.getUser().isShiftKeyDown()?-1:1);
                    nbt.putInt("mode",newMode);
                    userPower.getUser().getCapability(LivingDataProvider.CAPABILITY).ifPresent(data->{
                        data.setMode(newMode);
                    });
                }
            }

        }
    }

    private final LazySupplier<ResourceLocation> hostile =
            new LazySupplier<>(() -> makeIconVariant(this, "_hostile"));
    private final LazySupplier<ResourceLocation> player =
            new LazySupplier<>(() -> makeIconVariant(this, "_player"));
    private final LazySupplier<ResourceLocation> none =
            new LazySupplier<>(() -> makeIconVariant(this, "_none"));

    @Override
    protected ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if(power.getUser() != null){
            Hand hand = power.getUser().getItemInHand(Hand.MAIN_HAND).getItem() == InitItems.EMPEROR.get()? Hand.MAIN_HAND:Hand.OFF_HAND;
            ItemStack itemStack =power.getUser().getItemInHand(hand);
            if(itemStack.getItem() == InitItems.EMPEROR.get()){
                if(itemStack.hasTag()){
                    CompoundNBT nbt = itemStack.getTag();
                    if(nbt != null && nbt.get("mode") != null){
                        switch (Math.abs(nbt.getInt("mode")%4)){
                            case 1: return hostile.get();
                            case 2: return player.get();
                            case 0: return none.get();
                        }
                    }
                }
            }
        }
        return super.getIconTexturePath(power);
    }

    private static final TranslationTextComponent HOSTILE = new TranslationTextComponent("rotp_zemperor.type.hostile");
    private static final TranslationTextComponent PLAYERS = new TranslationTextComponent("rotp_zemperor.type.players");
    private static final TranslationTextComponent RANDOM = new TranslationTextComponent("rotp_zemperor.type.random");
    private static final TranslationTextComponent NONE = new TranslationTextComponent("rotp_zemperor.type.non");

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        TranslationTextComponent target = NONE;
        if(power.getUser() != null){
            Hand hand = power.getUser().getItemInHand(Hand.MAIN_HAND).getItem() == InitItems.EMPEROR.get()? Hand.MAIN_HAND:Hand.OFF_HAND;
            ItemStack itemStack =power.getUser().getItemInHand(hand);
            if(itemStack.getItem() == InitItems.EMPEROR.get()){
                if(itemStack.hasTag()){
                    CompoundNBT nbt = itemStack.getTag();
                    if(nbt != null && nbt.get("mode") != null){
                        switch (nbt.getInt("mode")%4){
                            case 1:target = HOSTILE;
                            break;
                            case 2:target = PLAYERS;
                            break;
                            case 3: target = RANDOM;
                            break;
                            default: target = NONE;
                            break;
                        }
                    }
                }
            }
        }
        return new TranslationTextComponent(key,target);
    }
}
