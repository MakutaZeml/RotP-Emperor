package com.zeml.rotp_zhp.util;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonSkills;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mc.damage.StandDamageSource;
import com.zeml.rotp_zhp.RotpEmperorAddon;
import com.zeml.rotp_zhp.init.InitItems;
import com.zeml.rotp_zhp.init.InitStands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = RotpEmperorAddon.MOD_ID)
public class GameplayHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof ItemEntity){
            ItemEntity entItem = (ItemEntity) entity;
            if(entItem.getItem().getItem() == InitItems.EMPEROR.get()){
                entity.remove();
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;


        IStandPower.getStandPowerOptional(player).ifPresent(
                standPower -> {
                    StandType<?> emp = InitStands.STAND_EMPEROR.getStandType();
                    if(standPower.getType()!=emp){
                        empInv(player);
                    }else if (standPower.getStandManifestation() instanceof StandEntity){
                        if(player.getItemInHand(Hand.MAIN_HAND).getItem() != InitItems.EMPEROR.get() && player.getItemInHand(Hand.OFF_HAND).getItem() != InitItems.EMPEROR.get()){

                            ItemStack hand = player.getItemInHand(Hand.MAIN_HAND);
                            if(!hand.isEmpty()){
                                ItemEntity ent = new ItemEntity(player.level,player.getX(),player.getY(),player.getZ(),hand);
                                player.level.addFreshEntity(ent);
                            }

                            ItemStack itemStack = new ItemStack(InitItems.EMPEROR.get(),1);
                            player.setItemInHand(Hand.MAIN_HAND,itemStack);
                            oneEmp(player);
                        }
                    }else {
                        empInv(player);
                    }
                }
        );
    }


    private static void empInv(PlayerEntity player){
        for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
            ItemStack inventoryStack = player.inventory.getItem(i);
            if (inventoryStack.getItem() == InitItems.EMPEROR.get()) {
                inventoryStack.shrink(inventoryStack.getCount());
            }
        }
    }


    private static void oneEmp(PlayerEntity player) {

        int selected = player.inventory.selected;

        for (int i = 9; i < player.inventory.getContainerSize(); ++i){
            ItemStack inventoryStack = player.inventory.getItem(i);
            if (inventoryStack.getItem() == InitItems.EMPEROR.get()) {
                inventoryStack.shrink(inventoryStack.getCount());
            }
        }
            for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
            ItemStack inventoryStack = player.inventory.getItem(i);
            if (inventoryStack.getItem() == InitItems.EMPEROR.get()) {

                if (i!=selected){
                    inventoryStack.shrink(inventoryStack.getCount());
                }
            }
        }
    }
}
