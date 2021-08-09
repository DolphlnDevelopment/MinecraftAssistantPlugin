package com.dolphln.minecraftassistant.commands;

import com.dolphln.minecraftassistant.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {

    private final Main plugin;

    public LinkCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You cannot execute this command as a console.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "You need to download the client, configure the config to connect to this server's ip and the port " + plugin.getConfigFile().getConfig().getInt("port") + " and run the link command that's shown in the console.");
            return false;
        }

        String arg = args[0];

        try {
            int code = Integer.parseInt(arg);
            plugin.getSocketServer().linkCode(((Player) sender).getUniqueId(), code);
            sender.sendMessage(ChatColor.YELLOW + "Trying to connect to your client... Try saying any command to know if the connection worked... (I'm lazy lol)");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid code. Run /link for instructions.");
            return false;
        }

        return true;
    }
}
