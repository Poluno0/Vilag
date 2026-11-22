package ru.example.villagercontrol;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;

public class VillagerControlCommands {
    public static void register(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("villagers")
            .requires(source -> source.hasPermission(2))
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("запретить")
                .executes(ctx -> {
                    VillagerControlData.get(ctx.getSource().getServer().overworld()).setSpawnBlocked(true);
                    ctx.getSource().sendSuccess(() -> Component.literal("§cСпавн обычных жителей запрещён."), true);
                    return 1;
                }))
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("разрешить")
                .executes(ctx -> {
                    VillagerControlData.get(ctx.getSource().getServer().overworld()).setSpawnBlocked(false);
                    ctx.getSource().sendSuccess(() -> Component.literal("§aСпавн обычных жителей разрешён."), true);
                    return 1;
                }))
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("убить")
                .executes(ctx -> {
                    int count = 0;
                    AABB area = new AABB(-3.0E7, -2048.0, -3.0E7, 3.0E7, 2048.0, 3.0E7);
                    for (ServerLevel level : ctx.getSource().getServer().getAllLevels()) {
                        var villagers = level.getEntitiesOfClass(Villager.class, area);
                        count += villagers.size();
                        villagers.forEach(Villager::kill);
                    }
                    final int killed = count;
                    ctx.getSource().sendSuccess(() -> Component.literal("§cУдалено обычных жителей: " + killed), true);
                    return count;
                }))
            .then(LiteralArgumentBuilder.<CommandSourceStack>literal("статус")
                .executes(ctx -> {
                    boolean blocked = VillagerControlData.get(ctx.getSource().getServer().overworld()).isSpawnBlocked();
                    ctx.getSource().sendSuccess(() -> Component.literal(blocked
                        ? "§cСпавн обычных жителей запрещён."
                        : "§aСпавн обычных жителей разрешён."), false);
                    return 1;
                })));
    }
}
