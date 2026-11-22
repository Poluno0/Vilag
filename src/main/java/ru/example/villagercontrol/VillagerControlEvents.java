package ru.example.villagercontrol;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VillagerControlMod.MOD_ID)
public class VillagerControlEvents {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.loadedFromDisk()) return; // Существующие жители из сохранения не блокируются.
        if (event.getEntity().getType() != EntityType.VILLAGER
                && event.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) return;

        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            if (VillagerControlData.get(serverLevel.getServer().overworld()).isSpawnBlocked()) {
                event.setCanceled(true);
            }
        }
    }
}
