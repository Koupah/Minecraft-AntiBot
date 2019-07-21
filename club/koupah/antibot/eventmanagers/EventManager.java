package club.koupah.antibot.eventmanagers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import club.koupah.antibot.Main;
import club.koupah.antibot.constructors.PossibleBot;

public class EventManager implements Listener {
	
	public static Map<Player,PossibleBot> suspects = new HashMap<Player,PossibleBot>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		suspects.put(event.getPlayer(),new PossibleBot(event.getPlayer()));
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		suspects.remove(event.getPlayer());	
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		try {
		if (!suspects.get(event.getPlayer()).hasInteracted()) {
		suspects.get(event.getPlayer()).addGoodFlags(1);
		suspects.get(event.getPlayer()).setInteracted(true);
		}
		} catch (Exception e) {
			Main.out("Player \"" + event.getPlayer().getName() + "\" doesn't have a PossibleBot constructor!");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityHit(EntityDamageByEntityEvent event) {
		try {
			if (event.getDamager() instanceof Player)
		if (!suspects.get(event.getDamager()).hasInteracted()) {
		suspects.get(event.getDamager()).addGoodFlags(1);
		suspects.get(event.getDamager()).setInteracted(true);
		}
		} catch (Exception e) {
			if (event.getDamager() instanceof Player)
			Main.out("Player \"" + ((Player)event.getDamager()).getName() + "\" doesn't have a PossibleBot constructor!");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		try {
		if (!suspects.get(event.getPlayer()).hasChatted()) {
		suspects.get(event.getPlayer()).addGoodFlags(1);
		suspects.get(event.getPlayer()).setChatted(true);
		}
		} catch (Exception e) {
			Main.out("Player \"" + event.getPlayer().getName() + "\" doesn't have a PossibleBot constructor!");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessage(AsyncPlayerChatEvent event) {
		try {
		if (!suspects.get(event.getPlayer()).hasChatted()) {
		suspects.get(event.getPlayer()).addGoodFlags(1);
		suspects.get(event.getPlayer()).setChatted(true);
		}
		} catch (Exception e) {
			Main.out("Player \"" + event.getPlayer().getName() + "\" doesn't have a PossibleBot constructor!");
		}
	}


}
