package com.nexus.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.nexus.NexusClient;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class Commands {
    private Commands() {}

    private static void msg(FabricClientCommandSource src, String text) {
        src.sendFeedback(Component.literal("§b[Nexus] §f" + text));
    }

    private static List<String> moduleNames() {
        return NexusClient.modules.all().stream().map(m -> m.name).toList();
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("nx")
                .executes(ctx -> {
                    msg(ctx.getSource(), "Komendy: /nx list | /nx toggle <modul> | /nx set <modul> <opcja> <wartosc> | /nx info <modul>");
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("list").executes(ctx -> {
                    for (Module m : NexusClient.modules.all()) {
                        msg(ctx.getSource(), (m.isEnabled() ? "§a[ON]  " : "§7[OFF] ") + "§f" + m.name
                            + " §8(" + m.category.name().toLowerCase() + ") §7- " + m.description);
                    }
                    return Command.SINGLE_SUCCESS;
                }))
                .then(literal("toggle")
                    .then(argument("modul", StringArgumentType.word())
                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(moduleNames(), b))
                        .executes(ctx -> {
                            Module m = NexusClient.modules.get(StringArgumentType.getString(ctx, "modul"));
                            if (m == null) { msg(ctx.getSource(), "§cNie ma takiego modulu."); return 0; }
                            m.toggle();
                            msg(ctx.getSource(), m.name + (m.isEnabled() ? " §awlaczony" : " §cwylaczony"));
                            return Command.SINGLE_SUCCESS;
                        })))
                .then(literal("info")
                    .then(argument("modul", StringArgumentType.word())
                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(moduleNames(), b))
                        .executes(ctx -> {
                            Module m = NexusClient.modules.get(StringArgumentType.getString(ctx, "modul"));
                            if (m == null) { msg(ctx.getSource(), "§cNie ma takiego modulu."); return 0; }
                            msg(ctx.getSource(), m.name + ": " + m.description);
                            for (Setting s : m.settings) {
                                msg(ctx.getSource(), " §7" + s.name + " = §f" + s.get() + " §8(" + s.min + " - " + s.max + ")");
                            }
                            return Command.SINGLE_SUCCESS;
                        })))
                .then(literal("set")
                    .then(argument("modul", StringArgumentType.word())
                        .suggests((ctx, b) -> SharedSuggestionProvider.suggest(moduleNames(), b))
                        .then(argument("opcja", StringArgumentType.word())
                            .suggests((ctx, b) -> {
                                Module m = NexusClient.modules.get(StringArgumentType.getString(ctx, "modul"));
                                List<String> names = m == null ? List.of() : m.settings.stream().map(s -> s.name).toList();
                                return SharedSuggestionProvider.suggest(names, b);
                            })
                            .then(argument("wartosc", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    Module m = NexusClient.modules.get(StringArgumentType.getString(ctx, "modul"));
                                    if (m == null) { msg(ctx.getSource(), "§cNie ma takiego modulu."); return 0; }
                                    Setting s = m.getSetting(StringArgumentType.getString(ctx, "opcja"));
                                    if (s == null) { msg(ctx.getSource(), "§cNie ma takiej opcji."); return 0; }
                                    s.set(DoubleArgumentType.getDouble(ctx, "wartosc"));
                                    msg(ctx.getSource(), m.name + "." + s.name + " = " + s.get());
                                    return Command.SINGLE_SUCCESS;
                                })))))
            ));
    }
}
