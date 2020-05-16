package me.stan.hatmanager.Commands;

import me.stan.hatmanager.Commands.SubCommands.*;
import me.stan.hatmanager.HatManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.logging.Logger;

public class HatCommandManager implements CommandExecutor, TabCompleter {
    private HatManager plugin;
    private Logger logger;
    private SubCommand defaultCommand;
    private Map<String, SubCommand> dreamCommands = new LinkedHashMap<>();
    private Map<String, SubCommand> dreamCommandAliases = new LinkedHashMap<>();

    public HatCommandManager(HatManager plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        logger.info("Setting up Credit Command Manager...");
        setupDefaultCommand();
        setupCommands();
    }

    public void setupCommands() {
        addCommand(new AddHatCommand(plugin,"add","Gives a hat to a player","add <PlayerName> <Hat ID/Name>",new Permission(plugin.getConfigManager().getDefaultConfig().getHatAddPerm())));
        addCommand(new RemoveHatCommand(plugin,"remove","Removes hat from a player","remove <PlayerName> <Hat ID/Name>",new Permission(plugin.getConfigManager().getDefaultConfig().getHatRemovePerm())));
        addCommand(new EquipHatCommand(plugin,"equip","Equips a hat","equip <Hat ID/Name>",new Permission(plugin.getConfigManager().getDefaultConfig().getHatEquipPerm())));
        addCommand(new ReloadConfigCommand(plugin,"reloadconfig","Reloads the messages config","reloadconfig",new Permission("hats.reloadconfig")));
    }

    private void setupDefaultCommand() {
        defaultCommand = new HelpCommand(plugin,"help","Displays help","help <page>");
        addCommand(defaultCommand);
    }

    private void addCommand(SubCommand command) {
        String commandName = command.getCommandName();
        List<String> commandAliases = command.getCommandAliases();
        if (checkIfCommandExists(commandName)) {
            logger.warning("Can't register command: [" + commandName + "] Reason: Command already exists!");
            return;
        }
        dreamCommands.put(commandName,command);
        logger.info("Registering command: [" + commandName + "]");
        if (commandAliases != null && !commandAliases.isEmpty()) {
            addAliases(command);
        }
    }

    private void addAliases(SubCommand command) {
        List<String> aliases = command.getCommandAliases();
        for (String alias : aliases) {
            if (checkIfCommandExists(alias)) {
                logger.warning("Can't register alias: [" + alias + "] under command: [" + command.getCommandName() + "] Reason: A command/alias is already registered with this name! \n" +
                        " Skipping registering alias...");
                continue;
            }
            logger.info("Registering alias: [" + alias + "] under command: [" + command.getCommandName() + "]");
            dreamCommandAliases.put(alias,command);
        }
    }

    private boolean checkIfCommandExists(String commandName) {
        return (dreamCommands.containsKey(commandName) || dreamCommandAliases.containsKey(commandName));
    }

    private SubCommand getCommand(String commandName) {
        SubCommand command = dreamCommands.get(commandName);
        if (command != null) {
            return command;
        }
        return dreamCommandAliases.get(commandName);
    }

    public List<String> getCommandNames() {
        return new ArrayList<>(dreamCommands.keySet());
    }

    public List<String> getPlayerCommandNames(Player player) {
        List<String> commandNames = new ArrayList<>();
        for (SubCommand command : dreamCommands.values()) {
            if (command.getPermission() == null || player.hasPermission(command.getPermission())) {
                commandNames.add(command.getCommandName());
            }
        }
        return commandNames;
    }

    public List<SubCommand> getPlayerCommands(Player player) {
        List<SubCommand> commands = new ArrayList<>();
        for (SubCommand command : dreamCommands.values()) {
            if (command.getPermission() == null || player.hasPermission(command.getPermission())) {
                commands.add(command);
            }
        }
        commands.remove(defaultCommand);
        return commands;
    }

    public SubCommand getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String[] newArgs = new String[0];
            if (args.length > 0) {
                String subCommandName = args[0];
                SubCommand dCommand = getCommand(subCommandName);
                if (dCommand == null) {
                    player.sendRawMessage(ChatColor.RED + "This command doesn't exist.");
                    return true;
                }
                if (dCommand.getPermission() == null || player.hasPermission(dCommand.getPermission())) {
                    if (args.length > 1) {
                        newArgs = Arrays.copyOfRange(args, 1, args.length);
                    }
                    return dCommand.execute(player,subCommandName,label,newArgs);
                } else {
                    player.sendRawMessage(ChatColor.RED + "You don't have permission to use this command!");
                }
            } else {
                return defaultCommand.execute(player, defaultCommand.getCommandName(), label, args);
            }
        } else {
            sender.sendMessage("Only players can use commands!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String[] newArgs = new String[0];
            if (args.length > 0) {
                String subCommandName = args[0];
                SubCommand dCommand = getCommand(subCommandName);
                if (dCommand != null) {
                    if (dCommand.getPermission() == null || player.hasPermission(dCommand.getPermission())) {
                        if (args.length > 1) {
                            newArgs = Arrays.copyOfRange(args, 1, args.length);
                        }
                        return dCommand.tabComplete(player,subCommandName,newArgs);
                    }
                } else {
                    return StringUtil.copyPartialMatches(subCommandName, getPlayerCommandNames(player), new ArrayList<>());
                }
            }
        }
        return Collections.emptyList();
    }
}
