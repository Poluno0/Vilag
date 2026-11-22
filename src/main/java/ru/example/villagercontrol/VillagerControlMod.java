package ru.example.villagercontrol;

import net.minecraftforge.fml.common.Mod;

@Mod(VillagerControlMod.MOD_ID)
public class VillagerControlMod {
    public static final String MOD_ID = "villagercontrol";

    public VillagerControlMod() {
        // Команды и события автоматически регистрируются через EventBusSubscriber.
    }
}
