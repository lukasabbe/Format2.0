package me.lukasabbe.format;

import me.lukasabbe.format.command.FormatCommand;
import me.lukasabbe.format.events.LeftClickEvent;
import me.lukasabbe.format.events.RightClickEvent;
import me.lukasabbe.format.objects.PlayerObject;
import me.lukasabbe.format.util.CalcType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.ArrayList;
import java.util.List;

public class Format implements ModInitializer, ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect{
    public static List<PlayerObject> players = new ArrayList<>();
    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register(this);
        ServerPlayConnectionEvents.DISCONNECT.register(this);
        UseBlockCallback.EVENT.register(new RightClickEvent());
        AttackBlockCallback.EVENT.register(new LeftClickEvent());
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(CommandManager.literal("format")
                    .then(CommandManager.literal("nbt").executes(ctx -> new FormatCommand().run(ctx, CalcType.nbt)))
                    .then(CommandManager.literal("json").executes(ctx -> new FormatCommand().run(ctx, CalcType.json))));
        });
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        players.removeIf(t -> t.playerUUID == serverPlayNetworkHandler.getPlayer().getUuid());
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        players.add(new PlayerObject(serverPlayNetworkHandler.player.getUuid()));
    }
}
