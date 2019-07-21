package club.koupah.antibot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

import club.koupah.antibot.Main;

public class AntiBotLockdownCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String string, final String[] args) {
    	if (args.length < 1) {
    		sender.sendMessage(ChatColor.AQUA + "(AntiBot) " + ChatColor.RED + "Missing parameter! (true/false)");
    	} else {
    		boolean option = false;
    		try {
    			option = Boolean.parseBoolean(args[0]);
    		} catch (Exception e) {
    			sender.sendMessage(ChatColor.AQUA + "(AntiBot) " + ChatColor.RED + "Invalid parameter! (true/false)");
    	    }
    		Main.lockdown = option;
    		sender.sendMessage(ChatColor.AQUA + "(AntiBot) " + ChatColor.GREEN + "Set lockdown to " + (option ? ChatColor.GREEN : ChatColor.RED) + option);
    	}
        return true;
    }
}
