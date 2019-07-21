package club.koupah.antibot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

public class AntiBotCreditCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String string, final String[] args) {
    sender.sendMessage(ChatColor.GRAY + "------------------");
    sender.sendMessage(ChatColor.AQUA + "AntiBot " + ChatColor.GREEN + " was made by " + ChatColor.LIGHT_PURPLE + "Koupah ");
    sender.sendMessage(ChatColor.GRAY + "------------------");
       return true;
    }
}
