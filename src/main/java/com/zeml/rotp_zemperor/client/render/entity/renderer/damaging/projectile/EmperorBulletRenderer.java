package com.zeml.rotp_zemperor.client.render.entity.renderer.damaging.projectile;

import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.github.standobyte.jojo.entity.damaging.projectile.TommyGunBulletEntity;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.zeml.rotp_zemperor.client.render.entity.model.projectile.EmperorBulletModel;
import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EmperorBulletRenderer extends EntityRenderer<EmperorBullet> {
    protected double maxTrailLen = 4;
    protected float V1 = 0.015625f;
    protected float BEAM_WIDTH = 0.015f;
    protected double BULLET_U = 0.015625;
    private static final ResourceLocation TRAIL_TEX = new ResourceLocation(RotpEmperorAddon.MOD_ID, "textures/entity/projectiles/bullet_trace.png");


    public EmperorBulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EmperorBullet entity, float yRotation, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        List<Vector3d> trace = entity.tracePos;
        if (trace.isEmpty()) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.translate(0, entity.getBbHeight() / 2, 0);
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity)));

        double traceLen = maxTrailLen;
        int i;
        boolean first = true;
        for (i = trace.size() - 1; i > 0 && traceLen > 0; i--) {
            Vector3d posCur = trace.get(i);
            Vector3d posPrev = trace.get(i - 1);
            float u0;
            float u1 = (float) (traceLen / maxTrailLen);

            Vector3d diffBack = posPrev.subtract(posCur);
            double len = diffBack.length();

            if (len > traceLen) {
                posPrev = posCur.add(diffBack.normalize().scale(traceLen));
                traceLen = 0;
            } else {
                traceLen -= len;
            }
            u0 = (float) (traceLen / maxTrailLen);

            trailSegment(posPrev, posCur, u0, u1, matrixStack, vertexBuilder, entity, yRotation, partialTick, first);
            first = false;
        }

        matrixStack.popPose();
        super.render(entity, yRotation, partialTick, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EmperorBullet p_110775_1_) {
        return TRAIL_TEX;
    }


    @Override
    public boolean shouldRender(EmperorBullet entity, ClippingHelper pCamera, double pCamX, double pCamY, double pCamZ) {
        if(!ClientUtil.canSeeStands()) return false;
        return super.shouldRender(entity, pCamera, pCamX, pCamY, pCamZ) ||
                entity.initialPos != null && pCamera.isVisible(new AxisAlignedBB(entity.initialPos, entity.position()));
    }

    protected void trailSegment(Vector3d pos1, Vector3d pos2, float u0, float u1,
                                MatrixStack matrixStack, IVertexBuilder vertexBuilder,
                                EmperorBullet entity, float yRotation, float partialTick, boolean first) {
        matrixStack.pushPose();
        Vector3d trailSegmentVec = pos1.subtract(pos2);
        float yRot = MathUtil.yRotDegFromVec(trailSegmentVec);
        float xRot = MathUtil.xRotDegFromVec(trailSegmentVec);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F - yRot));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-xRot));
        matrixStack.scale(1.0F, BEAM_WIDTH, BEAM_WIDTH);
        Matrix3f lighting = matrixStack.last().normal();
        lighting.setIdentity();
        ActiveRenderInfo camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        lighting.mul(Vector3f.XP.rotationDegrees(camera.getXRot()));
        float length = (float) trailSegmentVec.length();

        if (first) {
            renderFront(matrixStack, new Vector3f(0, 0, 1), vertexBuilder);
        }
        renderSide(matrixStack, new Vector3f(0, -1,  0),  length, u0, u1, vertexBuilder);
        renderSide(matrixStack, new Vector3f(0,  0, -1),  length, u0, u1, vertexBuilder);
        renderSide(matrixStack, new Vector3f(0,  1,  0),  length, u0, u1, vertexBuilder);
        renderSide(matrixStack, new Vector3f(0,  0,  1),  length, u0, u1, vertexBuilder);

        matrixStack.popPose();
        matrixStack.translate(trailSegmentVec.x, trailSegmentVec.y, trailSegmentVec.z);
    }

    private void renderSide(MatrixStack matrixStack, Vector3f lightNormal, float length, float u0, float u1, IVertexBuilder vertexBuilder) {
        int packedLight = ClientUtil.MAX_MODEL_LIGHT;
        float v0 = 0;
        float v1 = V1;
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        matrixStack.pushPose();

        matrixStack.translate(0, 0, 1f);

        MatrixStack.Entry matrix = matrixStack.last();
        Matrix4f pose = matrix.pose();
        Matrix3f normal = matrix.normal();
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                0, -1, 0,
                u1, v0,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                length, -1, 0,
                u0, v0,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                length, 1, 0,
                u0, v1,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                0, 1, 0,
                u1, v1,
                lightNormal.x(), lightNormal.y(), lightNormal.z());

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                0, -1, 0,
                u1, v0,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                length, -1, 0,
                u0, v0,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                length, 1, 0,
                u0, v1,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                0, 1, 0,
                u1, v1,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());

        matrixStack.popPose();
    }

    private void renderFront(MatrixStack matrixStack, Vector3f lightNormal, IVertexBuilder vertexBuilder) {
        int packedLight = ClientUtil.MAX_MODEL_LIGHT;
        float u0 = 0;
        float u1 = u0 + V1;
        float v0 = V1;
        float v1 = v0 + V1;
        matrixStack.pushPose();

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        MatrixStack.Entry matrix = matrixStack.last();
        Matrix4f pose = matrix.pose();
        Matrix3f normal = matrix.normal();
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                -1, -1, 0,
                u1, v0,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                1, -1, 0,
                u0, v0,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                1, 1, 0,
                u0, v1,
                lightNormal.x(), lightNormal.y(), lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                -1, 1, 0,
                u1, v1,
                lightNormal.x(), lightNormal.y(), lightNormal.z());

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                -1, -1, 0,
                u1, v0,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                1, -1, 0,
                u0, v0,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                1, 1, 0,
                u0, v1,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());
        ClientUtil.vertex(pose, normal, vertexBuilder,
                packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1,
                -1, 1, 0,
                u1, v1,
                -lightNormal.x(), -lightNormal.y(), -lightNormal.z());

        matrixStack.popPose();

    }


}
