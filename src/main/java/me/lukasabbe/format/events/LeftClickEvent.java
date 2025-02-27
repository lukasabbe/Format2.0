package me.lukasabbe.format.events;

import me.lukasabbe.format.Format;
import me.lukasabbe.format.objects.PlayerObject;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class LeftClickEvent implements AttackBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
        if(!playerEntity.getMainHandStack().isOf(Items.GOLDEN_AXE)) return ActionResult.PASS;

        if(!playerEntity.isCreative()) return ActionResult.PASS;

        Optional<PlayerObject> p = Format.players.stream().filter(t -> t.playerUUID == playerEntity.getUuid()).findFirst();
        if(p.isEmpty()) return ActionResult.PASS;


        PlayerObject playerObject = p.get();

        if(playerObject.firstPos == null || !playerObject.firstPos.equals(blockPos)){
            playerEntity.sendMessage(Text.literal("First corner picked"),false);
        }

        playerObject.firstPos = blockPos;


        return ActionResult.SUCCESS;
    }
}
