package com.zeml.rotp_zemperor.client.render.entity.renderer.damaging.projectile;

import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.zeml.rotp_zemperor.client.render.entity.model.projectile.EmperorBulletModel;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EmperorBulletRenderer extends SimpleEntityRenderer<EmperorBullet, EmperorBulletModel> {
    public EmperorBulletRenderer(EntityRendererManager renderManager) {
        super(renderManager, new EmperorBulletModel(), new ResourceLocation(JojoMod.MOD_ID, "textures/entity/projectiles/tommy_gun_bullet.png"));
    }

    @Override
    public void render(EmperorBullet entity, float yRotation, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {

    }
}
