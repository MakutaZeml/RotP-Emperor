package com.zeml.rotp_zemperor.client.render.entity.model.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class EmperorBulletModel extends EntityModel<EmperorBullet> {
    private final ModelRenderer bullet;

    public EmperorBulletModel() {
        texWidth = 8;
        texHeight = 8;
        bullet = new ModelRenderer(this);
        bullet.setPos(0.0F, 0.0F, 0.0F);
        bullet.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, -0.1F, false);
        bullet.texOffs(0, 3).addBox(-1.0F, -1.0F, 1.3F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        bullet.texOffs(0, 0).addBox(-1.0F, -1.0F, 2.1F, 1.0F, 1.0F, 0.0F, -0.1F, false);
    }

    @Override
    public void setupAnim(EmperorBullet entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        bullet.yRot = yRotationOffset * ((float)Math.PI / 180F);
        bullet.xRot = xRotation * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bullet.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
