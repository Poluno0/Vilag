package ru.example.villagercontrol;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerControlEvents {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide || event.loadedFromDisk()) return;
        // Only ordinary villagers are blocked. Zombie villagers are unaffected.
        if (event.getEntity().getType() == EntityType.VILLAGER
                && event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel
                && VillagerControlData.get(serverLevel).isSpawnBlocked()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onZombieVillagerCure(LivingConversionEvent.Pre event) {
        if (!event.getEntity().level().isClientSide
                && event.getEntity() instanceof ZombieVillager
                && event.getOutcome() == EntityType.VILLAGER) {
            event.setCanceled(true);
            event.getEntity().kill();
        }
    }
}
