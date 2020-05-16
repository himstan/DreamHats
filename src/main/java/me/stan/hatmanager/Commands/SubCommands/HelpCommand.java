package me.stan.hatmanager.Commands.SubCommands;

import me.stan.hatmanager.Commands.SubCommand;
import me.stan.hatmanager.HatManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends SubCommand {

    public HelpCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        super(plugin, commandName, commandDescription, commandUsage, permission, commandAliases);
    }

    public HelpCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        super(plugin, commandName, commandDescription, commandUsage, permission);
    }

    public HelpCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
        super(plugin, commandName, commandDescription, commandUsage);
    }

    private final int maxTextLength = 65;
    private final int commandsPerPage = 5;

    @Override
    public boolean execute(Player player, String commandName, String label, String[] args) {
        int currentPage = 1;
        int maxPage = calculateMaxPages(player);
        if (maxPage == 0) {
            maxPage = 1;
        }
        if (args.length > 0) {
            if (StringUtils.isNumeric(args[0])) {
                int potentialPage = Integer.parseInt(args[0]);
                if (potentialPage >= 1 && potentialPage <= maxPage) {
                    currentPage = Integer.parseInt(args[0]);
                }
            }
        }
        String headerLine = getHeaderLine();
        String footerLine = getFooterLine(currentPage,maxPage);
        me.stan.hatmanager.Utils.ChatUtils.sendCenteredMessage(player,headerLine);
        printCommands(player, label, currentPage);
        me.stan.hatmanager.Utils.ChatUtils.sendCenteredMessage(player,footerLine);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String label, String[] args) {
        if (args.length == 1) {
            return getPossiblePages(player);
        }
        return Collections.emptyList();
    }

    private int calculateMaxPages(Player player) {
        return getPlugin().getHatCommandManager().getPlayerCommands(player).size() / commandsPerPage;
    }

    private void printCommands(Player player, String label, int currentPage) {
        List<SubCommand> commands = getPlugin().getHatCommandManager().getPlayerCommands(player);
        if (commands == null || commands.size() == 0) {
            player.sendRawMessage(ChatColor.GRAY + "     - " + ChatColor.WHITE + "There are no commands for you.");
            return;
        }
        for (int i = (currentPage * commandsPerPage) - commandsPerPage; i < currentPage * commandsPerPage; i++) {
            if (i >= commands.size()) {
                return;
            }
            SubCommand command;
            if ((command = commands.get(i)) == null) {
                return;
            }
            player.sendRawMessage(ChatColor.GRAY + "     - " + ChatColor.WHITE + command.getCommandDescription());
            player.sendRawMessage(ChatColor.WHITE + "        - " + ChatColor.GRAY + "" + "/" + label + " " + command.getCommandUsage());
            if (command.getCommandAliases() != null) {
                player.sendRawMessage(ChatColor.WHITE + "        -" +  " Aliases: <" + ChatColor.GRAY + command.aliasesToString() + ChatColor.WHITE + ">");
            }
        }
    }

    private List<String> getPossiblePages(Player player) {
        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= calculateMaxPages(player); i++) {
            pages.add(String.valueOf(i));
        }
        return pages;
    }

    private String getHeaderLine() {
        String headerText = getPlugin().getName() + " HELP";
        int headerTextLength = maxTextLength - headerText.length();
        return ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", headerTextLength / 2) + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + headerText + ChatColor.RESET + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", headerTextLength / 2);
    }

    private String getFooterLine(int currentPage, int maxPage) {
        String footerPage = "(" + currentPage + "/" + maxPage + ")";
        int footerPageLength = maxTextLength - footerPage.length() + 8;
        return ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", footerPageLength / 2) + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + footerPage + ChatColor.RESET + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", footerPageLength / 2);
    }
}
