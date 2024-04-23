package com.zeml.rotp_zhp.client.render.entity.model.stand;

import com.zeml.rotp_zhp.entity.stand.stands.EmperorEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.RotationAngle;
import net.minecraft.client.renderer.model.ModelRenderer;

public class HermitPurpleModel  extends HumanoidStandModel<EmperorEntity> {

	final ModelRenderer spine_r1;

	public HermitPurpleModel() {
		super();

		addHumanoidBaseBoxes(null);
		texWidth = 128;
		texHeight = 128;

		rightForeArm.texOffs(37, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, false);


		spine_r1 = new ModelRenderer(this);
		rightForeArm.addChild(spine_r1);
		setRotationAngle(spine_r1, 0.0F, -0.5672F, 0.0F);
		spine_r1.texOffs(59, 8).addBox(-2.0F, 4.0F, -2.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		spine_r1.texOffs(59, 8).addBox(-3.0F, 3.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		spine_r1.texOffs(59, 8).addBox(2.0F, 2.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);


	}


	@Override
	protected ModelPose initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(leftArm, 0, 0, -0),

		});
	}


}