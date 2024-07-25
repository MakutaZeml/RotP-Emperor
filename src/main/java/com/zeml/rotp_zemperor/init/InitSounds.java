package com.zeml.rotp_zemperor.init;

import com.github.standobyte.jojo.init.ModSounds;
import com.zeml.rotp_zemperor.RotpEmperorAddon;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpEmperorAddon.MOD_ID);

    public static final RegistryObject<SoundEvent> EMPEROR_SUMMON = SOUNDS.register("emp_summon",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID,"emp_summon")));

    public static final RegistryObject<SoundEvent> VOID =SOUNDS.register("void",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID,"void")));

    public static final RegistryObject<SoundEvent> EMPEROR_UNSUMMON = SOUNDS.register("emp_unsummon",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID,"emp_unsummon")));


    public static final RegistryObject<SoundEvent> USER_EMPEROR = SOUNDS.register("hol_emp",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID, "hol_emp"))
            );

    public static final RegistryObject<SoundEvent> EMP_SHOT =SOUNDS.register("emp_shot",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID,"emp_shot")));


    static final OstSoundList HOL_OSST = new OstSoundList(new ResourceLocation(RotpEmperorAddon.MOD_ID, "emp_ost"), SOUNDS);
}
