package com.zeml.rotp_zemperor.client;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.client.render.entity.renderer.damaging.projectile.EmperorBulletRenderer;
import com.zeml.rotp_zemperor.client.render.entity.renderer.stand.HermitPurpleRenderer;
import com.zeml.rotp_zemperor.init.AddonStands;

import com.zeml.rotp_zemperor.init.InitEntities;
import com.zeml.rotp_zemperor.init.InitItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpEmperorAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {

    private static final IItemPropertyGetter STAND_ITEM_INVISIBLE = (itemStack, clientWorld, livingEntity) -> {
        return !ClientUtil.canSeeStands() ? 1 : 0;
    };

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();;

        RenderingRegistry.registerEntityRenderingHandler(AddonStands.EMPEROR_STAND.getEntityType(), HermitPurpleRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(InitEntities.EMPEROR_BULLET.get(), EmperorBulletRenderer::new);

        event.enqueueWork(() -> {
            ItemModelsProperties.register(InitItems.EMPEROR.get(),
                    new ResourceLocation(RotpEmperorAddon.MOD_ID, "stand_invisible"),
                    STAND_ITEM_INVISIBLE);
        });
    }

}
