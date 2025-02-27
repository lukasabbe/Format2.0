package me.lukasabbe.format.events;

import me.lukasabbe.format.Format;
import me.lukasabbe.format.objects.PlayerObject;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.Optional;

public class RightClickEvent implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        if(!playerEntity.getMainHandStack().isOf(Items.GOLDEN_AXE)) return ActionResult.PASS;

        if(!playerEntity.isCreative()) return ActionResult.PASS;

        Optional<PlayerObject> p = Format.players.stream().filter(t -> t.playerUUID == playerEntity.getUuid()).findFirst();
        if(p.isEmpty()) return ActionResult.PASS;


        PlayerObject playerObject = p.get();

        if(playerObject.secondPos == null || !playerObject.secondPos.equals(blockHitResult.getBlockPos())){
            playerEntity.sendMessage(Text.literal("Second corner picked"),false);
        }

        playerObject.secondPos = blockHitResult.getBlockPos();


        return ActionResult.SUCCESS;
    }
}
