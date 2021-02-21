package me.walrus.supremehomes.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.*;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import cloud.commandframework.tasks.TaskConsumer;
import me.walrus.supremehomes.network.Home;
import me.walrus.supremehomes.network.PlayerData;
import me.walrus.supremehomes.util.Permissions;
import me.walrus.supremehomes.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class SupremeManager {


    private BukkitCommandManager<CommandSender> manager;
    private CommandConfirmationManager<CommandSender> confirmationManager;
    private AnnotationParser<CommandSender> annotationParser;
    private Plugin p;

    public SupremeManager(Plugin p) {
        this.p = p;

        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
                AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().build();


        final Function<CommandSender, CommandSender> mapperFunction = Function.identity();
        try {
            this.manager = new PaperCommandManager<CommandSender>(
                    /* Owning plugin */ p,
                    /* Coordinator function */ executionCoordinatorFunction,
                    /* Command Sender -> C */ mapperFunction,
                    /* C -> Command Sender */ mapperFunction
            );
        } catch (final Exception e) {
            p.getLogger().severe("Failed to initialize the command manager");
            /* Disable the plugin */
            p.getServer().getPluginManager().disablePlugin(p);
            return;
        }
        //
        // Register Brigadier mappings
        //
        if (manager.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            manager.registerBrigadier();
        }
        //
        // Register asynchronous completions
        //
        if (manager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            ((PaperCommandManager<CommandSender>) this.manager).registerAsynchronousCompletions();
        }

        //
        // Create the annotation parser. This allows you to define commands using methods annotated with
        // @CommandMethod
        //
        final Function<ParserParameters, CommandMeta> commandMetaFunction = parserParameters ->
                CommandMeta.simple()
                        // This will allow you to decorate commands with descriptions
                        .with(CommandMeta.DESCRIPTION, parserParameters.get(StandardParameters.DESCRIPTION, "No description"))
                        .build();
        this.annotationParser = new AnnotationParser<>(
                /* Manager */ this.manager,
                /* Command sender type */ CommandSender.class,
                /* Mapper for command meta instances */ commandMetaFunction
        );
        //
        // Create the commands
        //
        this.constructCommands();
    }

    private void constructCommands() {
        this.annotationParser.parse(new CmdHome(manager));
        this.annotationParser.parse(new CmdDeleteHome());
        this.annotationParser.parse(new CmdSetHome());
        this.annotationParser.parse(new CmdListHomes());

        final Command.Builder<CommandSender> builder = this.manager.commandBuilder("base");
    }
}
