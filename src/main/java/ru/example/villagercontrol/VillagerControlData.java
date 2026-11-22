package ru.example.villagercontrol;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class VillagerControlData extends SavedData {
    private static final String DATA_NAME = "villagercontrol_state";
    private boolean spawnBlocked;

    public VillagerControlData() {
        this.spawnBlocked = false;
    }

    public static VillagerControlData load(CompoundTag tag) {
        VillagerControlData data = new VillagerControlData();
        data.spawnBlocked = tag.getBoolean("spawnBlocked");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("spawnBlocked", spawnBlocked);
        return tag;
    }

    public static VillagerControlData get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().getLevel(ServerLevel.OVERWORLD);
        return overworld.getDataStorage().computeIfAbsent(VillagerControlData::load, VillagerControlData::new, DATA_NAME);
    }

    public boolean isSpawnBlocked() {
        return spawnBlocked;
    }

    public void setSpawnBlocked(boolean blocked) {
        this.spawnBlocked = blocked;
        setDirty();
    }
}
