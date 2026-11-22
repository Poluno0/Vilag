package ru.example.villagercontrol;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VillagerControlMod.MOD_ID)
public class VillagerControlCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();
        d.register(com.mojang.brigadier.builder.LiteralArgumentBuilder.<CommandSourceStack>literal("villagers")
            .requires(source -> source.hasPermission(2))
            .then(com.mojang.brigadier.builder.LiteralArgumentBuilder.<CommandSourceStack>literal("запретить")
                .executes(ctx -> {
                    VillagerControlData.get(ctx.getSource().getServer().overworld()).setSpawnBlocked(true);
                    ctx.getSource().sendSuccess(() -> Component.literal("§cСпавн жителей и зомби-жителей запрещён."), true);
                    return 1;
                }))
            .then(com.mojang.brigadier.builder.LiteralArgumentBuilder.<CommandSourceStack>literal("разрешить")
                .executes(ctx -> {
                    VillagerControlData.get(ctx.getSource().getServer().overworld()).setSpawnBlocked(false);
                    ctx.getSource().sendSuccess(() -> Component.literal("§aСпавн жителей и зомби-жителей разрешён."), true);
                    return 1;
                }))
            .then(com.mojang.brigadier.builder.LiteralArgumentBuilder.<CommandSourceStack>literal("убить")
                .executes(ctx -> {
                    int removed = 0;
                    AABB worldBounds = new AABB(-30000000, -2048, -30000000, 30000000, 2048, 30000000);
                    for (ServerLevel level : ctx.getSource().getServer().getAllLevels()) {
                        removed += level.getEntities(EntityType.VILLAGER, worldBounds, e -> true).size();
                        removed += level.getEntities(EntityType.ZOMBIE_VILLAGER, worldBounds, e -> true).size();
                        level.getEntities(EntityType.VILLAGER, worldBounds, e -> true).forEach(e -> e.discard());
                        level.getEntities(EntityType.ZOMBIE_VILLAGER, worldBounds, e -> true).forEach(e -> e.discard());
                    }
                    final int total = removed;
                    ctx.getSource().sendSuccess(() -> Component.literal("§eУдалено жителей и зомби-жителей: " + total), true);
                    return removed;
                }))
            .then(com.mojang.brigadier.builder.LiteralArgumentBuilder.<CommandSourceStack>literal("статус")
                .executes(ctx -> {
                    boolean blocked = VillagerControlData.get(ctx.getSource().getServer().overworld()).isSpawnBlocked();
                    ctx.getSource().sendSuccess(() -> Component.literal(blocked
                        ? "§cСпавн жителей сейчас запрещён."
                        : "§aСпавн жителей сейчас разрешён."), false);
                    return 1;
                }))
        );
    }
}
