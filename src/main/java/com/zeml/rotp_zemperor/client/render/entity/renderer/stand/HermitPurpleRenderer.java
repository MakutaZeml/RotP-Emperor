package com.zeml.rotp_zemperor.client.render.entity.renderer.stand;

import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.client.render.entity.model.stand.HermitPurpleModel;
import com.zeml.rotp_zemperor.entity.stand.stands.EmperorEntity;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class HermitPurpleRenderer  extends StandEntityRenderer<EmperorEntity, HermitPurpleModel> {

    public HermitPurpleRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HermitPurpleModel(), new ResourceLocation(RotpEmperorAddon.MOD_ID, "textures/entity/stand/void.png"), 0);
    }

}
