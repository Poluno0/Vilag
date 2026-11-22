package ru.example.villagercontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(VillagerControlMod.MOD_ID)
public class VillagerControlMod {
    public static final String MOD_ID = "villagercontrol";

    public VillagerControlMod() {
        MinecraftForge.EVENT_BUS.register(VillagerControlEvents.class);
        MinecraftForge.EVENT_BUS.addListener(VillagerControlCommands::register);
    }
}
