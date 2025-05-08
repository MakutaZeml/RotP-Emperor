package com.zeml.rotp_zemperor.client.playeranim.anim;

import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.github.standobyte.jojo.client.playeranim.anim.interfaces.WindupAttackAnim;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import net.minecraft.util.ResourceLocation;


public class AddonPlayerAnimations {
    public static WindupAttackAnim summon_emp;



    public static void init(){
        summon_emp = PlayerAnimationHandler.getPlayerAnimator().registerAnimLayer(
                "com.zeml.rotp_zemperor.client.playeranim.anim.kosmimpl.KosmXSummonEmperorHandler",
                new ResourceLocation(RotpEmperorAddon.MOD_ID, "summon_emp"), 1,
                WindupAttackAnim.NoPlayerAnimator::new);



    }
}
