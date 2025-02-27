package me.lukasabbe.format.objects;

import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class PlayerObject {
    public UUID playerUUID;
    public BlockPos firstPos;
    public BlockPos secondPos;
    public PlayerObject(UUID playerUUID){
        this.playerUUID = playerUUID;
    }
}
