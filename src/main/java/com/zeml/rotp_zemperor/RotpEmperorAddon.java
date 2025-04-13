package com.zeml.rotp_zemperor;

import com.zeml.rotp_zemperor.capability.CapabilityHandler;
import com.zeml.rotp_zemperor.init.InitEntities;
import com.zeml.rotp_zemperor.init.InitItems;
import com.zeml.rotp_zemperor.network.AddonPackets;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zeml.rotp_zemperor.init.InitSounds;
import com.zeml.rotp_zemperor.init.InitStands;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RotpEmperorAddon.MOD_ID)
public class RotpEmperorAddon {
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_zemperor";
    public static final Logger LOGGER = LogManager.getLogger();

    public RotpEmperorAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::preInit);
    }

    private void preInit(FMLCommonSetupEvent event){
        AddonPackets.init();
        CapabilityHandler.commonSetupRegister();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
