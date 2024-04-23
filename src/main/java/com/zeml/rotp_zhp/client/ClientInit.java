package com.zeml.rotp_zhp.client;

import com.zeml.rotp_zhp.RotpEmperorAddon;
import com.zeml.rotp_zhp.client.render.entity.renderer.damaging.projectile.EmperorBulletRenderer;
import com.zeml.rotp_zhp.client.render.entity.renderer.stand.HermitPurpleRenderer;
import com.zeml.rotp_zhp.init.AddonStands;

import com.zeml.rotp_zhp.init.InitEntities;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpEmperorAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();;

        RenderingRegistry.registerEntityRenderingHandler(AddonStands.EMPEROR_STAND.getEntityType(), HermitPurpleRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(InitEntities.EMPEROR_BULLET.get(), EmperorBulletRenderer::new);

    }

}
