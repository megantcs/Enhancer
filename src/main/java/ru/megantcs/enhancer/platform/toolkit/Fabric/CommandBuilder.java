package ru.megantcs.enhancer.platform.toolkit.Fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandBuilder
{
    private final String command;
    private final LiteralArgumentBuilder<FabricClientCommandSource> builder;
    private Action<CommandContext<FabricClientCommandSource>> defaultAction;


    protected CommandBuilder(String command) {
        this.command = command;
        this.builder = literal(command);
    }

    public CommandBuilder orArg(String argument, Action<CommandContext<FabricClientCommandSource>> action) {
        builder.then(literal(argument)
                .executes(context -> {
                    if(action != null) {
                        action.invoke(context);
                    }
                    return Command.SINGLE_SUCCESS;
                }));
        return this;
    }

    public CommandBuilder thenArg(String argument, Action<CommandContext<FabricClientCommandSource>> action) {
        return orArg(argument, action);
    }

    public CommandBuilder executes(Action<CommandContext<FabricClientCommandSource>> action) {
        this.defaultAction = action;
        builder.executes(context -> {
            if (defaultAction != null) {
                defaultAction.invoke(context);
            }
            return Command.SINGLE_SUCCESS;
        });
        return this;
    }

    public CommandBuilder withFeedback(String message) {
        return executes(context -> {
            context.getSource().sendFeedback(net.minecraft.text.Text.literal(message));
        });
    }

    public CommandBuilder register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(builder);
        });

        return this;
    }

    public static CommandBuilder create(String command) {
        return new CommandBuilder(command);
    }
}