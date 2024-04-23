package com.zeml.rotp_zhp.init;

import com.github.standobyte.jojo.JojoMod;
import com.zeml.rotp_zhp.RotpEmperorAddon;
import com.zeml.rotp_zhp.item.EmperorItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotpEmperorAddon.MOD_ID);


    public static final RegistryObject<EmperorItem> EMPEROR = ITEMS.register("emperor",
            ()->new EmperorItem(new Item.Properties().stacksTo(1)));


}
