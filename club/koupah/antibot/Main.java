package club.koupah.antibot;


import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import club.koupah.antibot.commands.AntiBotCreditCommand;
import club.koupah.antibot.commands.AntiBotLockdownCommand;
import club.koupah.antibot.constructors.PossibleBot;
import club.koupah.antibot.eventmanagers.EventManager;


public class Main extends JavaPlugin {

	static boolean isEnabled = true;
	  
	static Server server;

	protected static int maxFlags = 6;  
	protected static int lockdownFlags = 4;
	
	public static boolean lockdown = false;
	
	public void onEnable() {
		server = getServer();
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN+ "\n\nKoupah's AntiBot has been enabled!\n");
		getServer().getPluginManager().registerEvents(new EventManager(), this);	
		getCommand("antibotlockdown").setExecutor((CommandExecutor)new AntiBotLockdownCommand());
		getCommand("antibotcredit").setExecutor((CommandExecutor)new AntiBotCreditCommand());
		
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    @Override
		    public void run() {
		    	for (Map.Entry<Player, PossibleBot> entry : EventManager.suspects.entrySet()) {
		    		
		    		//Incase for some reason they don't disconnect    (or should be ignored)
		    		if (entry.getValue().isBanned() || entry.getValue().shouldIgnore())
		    			continue;
		    		
		    		
		    		//Reset their ping times every 20 seconds
		    		if (entry.getValue().getPingTimes().size() >= 20)
		    			entry.getValue().resetPingTimes();
		    		
		    		//Add their ping to ping arraylist (this happens once a second)
		    		entry.getValue().addPing();
		    		
		    		//If in lockdown
		    		if (isLockdown()) {
		    			
		    			//Check flags
		    			if (entry.getValue().getFlags() >= Main.lockdownFlags) {
		    				entry.getValue().setBanned(true);
		    				//Ban player (Litebans format)
		    				getServer().dispatchCommand(getServer().getConsoleSender(), "ban " + entry.getKey().getName() + " 10m AntiBot (LOCKDOWN MODE) -s");
			    			out(ChatColor.AQUA+"(AntiBot) " + ChatColor.RED + "Banned " + ChatColor.GREEN + entry.getKey().getName() + ChatColor.RED + " for having " + entry.getValue().getFlags() + " flags ( >= " + Main.lockdownFlags + ")");
			    		}	
		    			
		    			//If they have been facing the same way as they did when they logged in for more than 20 seconds
		    			if (!entry.getValue().hasFacingChanged(entry.getKey().getLocation()) && entry.getValue().getSecondsSinceLogin() >= 20 && !entry.getValue().hasFlaggedFacing()) {
		    				entry.getValue().addFlags(1);
		    				//Set to true to prevent flagging twice
		    				entry.getValue().setFlaggedFacing(true);
		    			}
		    			
		    			//Has moved check (if they haven't moved since login after 20 secs)
		    			if (entry.getValue().getSecondsSinceLogin() >= 20 && !entry.getValue().hasMoved(entry.getKey().getLocation()) && !entry.getValue().hasFlaggedMove()) {
			    			entry.getValue().addFlags(1);
			    			//To stop this from flagging twice
			    			entry.getValue().setFlaggedMove(true);
			    		}
		    			
		    			//Has interacted (if they haven't interacted in 20 seconds)
		    			if (!entry.getValue().hasInteracted() && entry.getValue().getSecondsSinceLogin() >= 30 && (!entry.getValue().hasMoved(entry.getKey().getLocation()) || !entry.getValue().hasFacingChanged(entry.getKey().getLocation()))) {
		    				entry.getValue().addFlags(1);
		    				//Set to true to stop this from flagging again
		    				entry.getValue().setInteracted(true);
		    			}
		    			
		    			//Has chatted (if they have chatted within 200 millis of login)         //getChatTime returns how many milliseconds after login their message was sent
		    			if (entry.getValue().hasChatted() && entry.getValue().getChatTime() != -1 && entry.getValue().getChatTime() <= 200) {
		    				entry.getValue().addFlags(2);
		    				//Set to -1 to stop it from flagging again
		    				entry.getValue().setChatTime(-1);
		    			}
		    			
		    			//0 ping 4 seconds in a row
		    			if (entry.getValue().isInvalidPing(4)) {
		    				entry.getValue().addFlags(2);
		    				entry.getValue().resetPingTimes();
		    			}
		    			
		    			//750+ ping 8 seconds in a row
		    			if (entry.getValue().shouldPingKick(0, 8)) {
		    				entry.getValue().addFlags(6);
		    				entry.getValue().resetPingTimes();
		    			}
		    			
		    			
		    		} else {
		    			if (entry.getValue().getFlags() >= Main.maxFlags ) {
		    				entry.getValue().setBanned(true);
		    				getServer().dispatchCommand(getServer().getConsoleSender(), "ban " + entry.getKey().getName() + " 10m AntiBot -s");
			    			out(ChatColor.AQUA+"(AntiBot) " + ChatColor.RED + "Banned " + ChatColor.GREEN + entry.getKey().getName() + ChatColor.RED + " for having " + entry.getValue().getFlags() + " flags ( >= " + Main.maxFlags + ")");
			    		}
		    			
		    			if (entry.getValue().isInvalidPing(8)) {
		    				entry.getValue().addFlags(2);
		    				entry.getValue().resetPingTimes();
		    			}
		    			
		    			if (entry.getValue().getSecondsSinceLogin() >= 100 && !entry.getValue().hasMoved(entry.getKey().getLocation()) && !entry.getValue().hasFacingChanged(entry.getKey().getLocation())) {
		    				getServer().dispatchCommand(getServer().getConsoleSender(), "kick " + entry.getKey() + " AntiBot -s");
			    		}		
		    			
		    		}
		    		
		    		//Ignore the good boys
		    		if (entry.getValue().hasChatted() && entry.getValue().hasInteracted() && entry.getValue().hasMoved(entry.getValue().getLocation()) && entry.getValue().hasFacingChanged(entry.getValue().getLocation())) {
		    			entry.getValue().setIgnore(true);
		    		}
		    		
		    
		    	}
		    }
		}, 0L, 20L); 
		
		
	}
	
	//Run this after adding/removing recipes or setting reversed boolean

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\nKoupah's \"Dev Application\" has been disabled!\n");
		isEnabled = false;
	}
	
	public static void out(Object i) {
		server.getConsoleSender().sendMessage(i+"");
	}
	
	public boolean isLockdown() {
		return lockdown;
	}

}
