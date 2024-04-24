package com.zeml.rotp_zemperor.init;

import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.item.EmperorItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotpEmperorAddon.MOD_ID);


    public static final RegistryObject<EmperorItem> EMPEROR = ITEMS.register("emperor",
            ()->new EmperorItem(new Item.Properties().stacksTo(1)));


}
