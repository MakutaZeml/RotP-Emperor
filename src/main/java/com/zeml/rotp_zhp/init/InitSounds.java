package com.zeml.rotp_zhp.init;

import com.github.standobyte.jojo.init.ModSounds;
import com.zeml.rotp_zhp.RotpEmperorAddon;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpEmperorAddon.MOD_ID);

    public static final RegistryObject<SoundEvent> EMPEROR_SUMMON = ModSounds.STAND_SUMMON_DEFAULT;

    public static final RegistryObject<SoundEvent> VOID =SOUNDS.register("void",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID,"void")));

    public static final RegistryObject<SoundEvent> EMPEROR_UNSUMMON = ModSounds.STAND_UNSUMMON_DEFAULT;


    public static final RegistryObject<SoundEvent> USER_EMPEROR = SOUNDS.register("hol_emp",
            ()->new SoundEvent(new ResourceLocation(RotpEmperorAddon.MOD_ID, "hol_emp"))
            );


    static final OstSoundList HOL_OSST = new OstSoundList(new ResourceLocation(RotpEmperorAddon.MOD_ID, "emp_ost"), SOUNDS);
}
