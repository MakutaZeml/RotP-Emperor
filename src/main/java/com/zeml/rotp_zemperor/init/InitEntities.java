package com.zeml.rotp_zemperor.init;

import com.github.standobyte.jojo.JojoMod;
import com.zeml.rotp_zemperor.RotpEmperorAddon;

import com.zeml.rotp_zemperor.entity.damaging.projectile.EmperorBullet;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RotpEmperorAddon.MOD_ID);
    
    public static final RegistryObject<EntityType<EmperorBullet>> EMPEROR_BULLET = ENTITIES.register("emperor_bullet",
            () -> EntityType.Builder.<EmperorBullet>of(EmperorBullet::new, EntityClassification.MISC).sized(0.0625F, 0.0625F).clientTrackingRange(4).setUpdateInterval(20).fireImmune()
                    .build(new ResourceLocation(JojoMod.MOD_ID, "emperor_bullet").toString()));

}
