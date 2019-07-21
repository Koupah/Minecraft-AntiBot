package club.koupah.antibot.constructors;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PossibleBot {

	ArrayList<Integer> pings = new ArrayList<Integer>();
	
	private Player player;
	
	private int flags;
	private int goodFlags;
	private Location location;
	private boolean interacted = false;
	private Long time;

	private float yaw;
	private float pitch;
	
	private boolean banned = false;

	private boolean chatted = false;
	private long chattedtime = -1;
	
	private boolean ignore = false;

	private boolean flaggedmove = false;

	private boolean flaggedfacing = false;
	
	public PossibleBot(Player p) {
		this.player = p;
		this.flags = 0;
		this.goodFlags = 0;
		this.location = p.getLocation();
		this.time = System.currentTimeMillis();
	
		this.yaw = p.getLocation().getYaw();
		this.pitch = p.getLocation().getPitch();
	}
	
	public boolean hasFacingChanged(Location loc) {
		return !(loc.getYaw() == this.yaw && loc.getPitch() == this.pitch);
	}
	
	public int getFlags() {
		return this.flags;
	}
	public int getGoodFlags() {
		return this.goodFlags;
	}
	public Player getPlayer() {
		return this.player;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public long getMillisSinceLogin() {
		return System.currentTimeMillis() - this.time;
	}

	public void addGoodFlags(int i) {
		this.goodFlags+=i;
	}
	public void addFlags(int i) {
		this.flags+=i;
	}
	
	public boolean hasInteracted() {
		return this.interacted;
	}
	
	public void setInteracted(boolean b) {
		this.interacted = b;
	}
	
	public boolean hasMoved(Location current) {
		return !current.toVector().equals(getLocation().toVector());
	}

	public boolean isBanned() {
		return this.banned ;
	}
	public void setBanned(boolean b) {
		this.banned = b;
	}

	public int getSecondsSinceLogin() {
		return (int) ((System.currentTimeMillis() - this.time) / 1000);
	}
	
	public boolean hasChatted() {
		return this.chatted ;
	}
	
	public void setChatted(boolean a) {
		this.chatted = a;
		this.chattedtime = this.time - System.currentTimeMillis();
	}
	
	public long getChatTime() {
		return this.chattedtime;
	}

	public void setChatTime(int i) {
		this.chattedtime = i;
	}
	
	public int getPing() {
		return ((CraftPlayer)this.player).getHandle().ping;
	}
	
	public ArrayList<Integer> getPingTimes() {
		return this.pings;
	}
	
	public void addPing() {
		this.pings.add(getPing());
	}
	
	public boolean shouldPingKick(int maxPing, int inARow) {
		//If in a row is more than ping times
		if (inARow > getPingTimes().size())
			return false;
		
		int count = 0;
		while (count <= inARow)
		for (Integer integer : getPingTimes()) {
			if (integer < maxPing)
				return false;
		}
		return true;
	}
	
	public void resetPingTimes() {
		this.pings.clear();
	}
	
	public boolean isInvalidPing(int maxViolations) {
		//If in a row is more than ping times
		if (maxViolations > getPingTimes().size())
			return false;
		
		int count = 0;
		while (count <= maxViolations)
		for (Integer integer : getPingTimes()) {
			if (integer != 0)
				return false;
		}
		return true;
	}

	public void setIgnore(boolean b) {
		this.ignore = b;
	}
	
	public boolean shouldIgnore() {
		return this.ignore;
	}

	public void setFlaggedMove(boolean b) {
		this.flaggedmove = b;
		
	}
	
	public boolean hasFlaggedMove() {
		return this.flaggedmove ;
	}
	
	public void setFlaggedFacing(boolean b) {
		this.flaggedfacing = b;
		
	}
	
	public boolean hasFlaggedFacing() {
		return this.flaggedfacing  ;
	}
	
}
