package me.lukasabbe.format.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lukasabbe.format.Format;
import me.lukasabbe.format.objects.BoxValue;
import me.lukasabbe.format.objects.PlayerObject;
import me.lukasabbe.format.util.CalcType;
import me.lukasabbe.format.util.FormatCalc;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class FormatCommand {

    public int run(CommandContext<ServerCommandSource> commandContext, CalcType type) throws CommandSyntaxException {
        if(!commandContext.getSource().isExecutedByPlayer()) {
            commandContext.getSource().sendError(Text.literal("The command can only be used by a player"));
            return 0;
        }

        Optional<PlayerObject> p = Format.players.stream().filter(t -> t.playerUUID == commandContext.getSource().getPlayer().getUuid()).findFirst();
        if(p.isEmpty()){
            commandContext.getSource().sendError(Text.literal("Something went wrong"));
            return 0;
        }

        PlayerObject playerObject = p.get();
        final BlockPos fPos = playerObject.firstPos;
        final BlockPos sPos = playerObject.secondPos;
        if(fPos == null || sPos == null){
            commandContext.getSource().sendError(Text.literal("Make sure to click to blocks before using the command"));
            return 0;
        }

        final String generatedString = FormatCalc.getStringValue(new BoxValue(fPos.getX(), fPos.getY(), fPos.getZ(), sPos.getX(), sPos.getY(), sPos.getZ()),type);
        final ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, generatedString);
        Text t = Text.literal(generatedString).setStyle(Style.EMPTY.withClickEvent(clickEvent));
        commandContext.getSource().getPlayer().sendMessage(t);
        return 1;
    }
}
