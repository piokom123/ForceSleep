package com.gmail.wolinskip.forcesleep.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.wolinskip.forcesleep.ForceSleep;
import com.gmail.wolinskip.forcesleep.Translate;
import com.gmail.wolinskip.forcesleep.data.SleepPlayer;
import com.gmail.wolinskip.forcesleep.data.SleepPlayers;

public class SleepPlayerListener implements Listener {
	Logger log	= Logger.getLogger("Minecraft");
	private ForceSleep plugin;
	private List<String> players;
	
	public SleepPlayerListener(ForceSleep plugin) {
		this.plugin = plugin;
		this.players = new ArrayList<String>();
	}
	
	/*
	 * starts counting time when user logging in
	 */
	@EventHandler
	public void onPlayerLogin(final PlayerJoinEvent event) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			public void run() {
				// skip if player have immunity
				if(event.getPlayer().hasPermission("forcesleep.immunity")) {
					event.getPlayer().sendMessage(ChatColor.GREEN + Translate.getString("ForceSleep: Immunity granted!"));
					return;
				}
				// get user data
				//PlayerData data 		= new PlayerData();
				SleepPlayer sleepPlayer = SleepPlayers.loadPlayerByName(event.getPlayer().getName());
				
				if(sleepPlayer == null) {
					event.getPlayer().sendMessage(ChatColor.RED + Translate.getString("ForceSleep can't track your activity! Please relog."));
					return;
				}
				
				//event.getPlayer().sendMessage("ForceSleep is watching you!");
		    }
		}, 40L);
	}
	
	/*
	 * resets time counter when player goes to bed
	 */
	@EventHandler
	public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
		// get user data
		//PlayerData data 		= new PlayerData();
		SleepPlayer sleepPlayer = SleepPlayers.loadPlayerByName(event.getPlayer().getName());
		
		if(sleepPlayer == null) {
			event.getPlayer().sendMessage(ChatColor.RED + Translate.getString("ForceSleep can't track your activity! Please relog."));
			return;
		}
		
		sleepPlayer.setFromLastSleep((long) 0);
		
		// make sure that next time warning will be showed asap
		sleepPlayer.setLastWarning((long) plugin.getConfig().getDouble("warning_delay"));
		
		SleepPlayers.updatePlayer(sleepPlayer);
		SleepPlayers.savePlayer(sleepPlayer.getName());
		
		event.getPlayer().sendMessage(ChatColor.GREEN + Translate.getString("You are rested, but do not forget to go to sleep in the next") + " " + plugin.getConfig().getDouble("sleep_delay")/60 + " " + Translate.getString("minutes"));
	}
	
	/*
	 * saves player profile on exit
	 */
	@EventHandler
	public void onPlayerExit(final PlayerQuitEvent event) {
		SleepPlayers.savePlayer(event.getPlayer().getName());
		SleepPlayers.removePlayer(event.getPlayer().getName());
	}
	
	/*
	 * checks online players, changing online time
	 */
	public void changeOnlineTime() {
		//SleepPlayers sleepPlayers = new SleepPlayers();
		SleepPlayer sleepPlayer = null;
		Player[] players = Bukkit.getOnlinePlayers();
		List<String> playersList = new ArrayList<String>();
		int count = players.length;
		if(count < 1) { // server is empty, don't do anything
			this.players.clear();
			return;
		}
		
		// add currently online players to tmp list
		for(int i=0;i<count;i++) {
			playersList.add(players[i].getName());
			
			// player was online last time, inc counter
			if(this.players.contains(players[i].getName())) {
				// get user data
				sleepPlayer = SleepPlayers.loadPlayerByName(players[i].getName());

				// error while reading player data, skip
				if(sleepPlayer == null || sleepPlayer.equals(null))
					continue;

				sleepPlayer.setTotalOnline(sleepPlayer.getTotalOnline() + plugin.getConfig().getInt("refresh_time"));
				sleepPlayer.setFromLastSleep(sleepPlayer.getFromLastSleep() + plugin.getConfig().getInt("refresh_time"));
				sleepPlayer.setLastRemind(sleepPlayer.getLastRemind() + plugin.getConfig().getInt("refresh_time"));
				// skip if player have immunity
				if(players[i].hasPermission("forcesleep.immunity")) {
					if(plugin.getConfig().getBoolean("debug")) {
						players[i].sendMessage(ChatColor.GREEN + Translate.getString("ForceSleep: Immunity!"));
					}
				} else {
					if(sleepPlayer.getFromLastSleep() > plugin.getConfig().getDouble("sleep_delay")) {
						sleepPlayer.setLastWarning(sleepPlayer.getLastWarning() + plugin.getConfig().getInt("refresh_time"));
						if(sleepPlayer.getLastWarning() > plugin.getConfig().getDouble("warning_delay")) {
						
							Long time = Bukkit.getPlayer(sleepPlayer.getName()).getWorld().getTime();
							if(time < 12300 || time > 23850) { // day
								if(Bukkit.getPlayer(sleepPlayer.getName()).getHealth() > plugin.getConfig().getDouble("minimum_health.day")) {
									Bukkit.getPlayer(sleepPlayer.getName()).sendMessage(ChatColor.RED + Translate.getString("You are very sleepy, go to bed as fast as you can!"));
									Bukkit.getPlayer(sleepPlayer.getName()).damage(1);
								}
							} else { // night
								if(Bukkit.getPlayer(sleepPlayer.getName()).getHealth() > plugin.getConfig().getDouble("minimum_health.night")) {
									Bukkit.getPlayer(sleepPlayer.getName()).sendMessage(ChatColor.RED + Translate.getString("You are very sleepy, go to bed as fast as you can!"));
									Bukkit.getPlayer(sleepPlayer.getName()).damage(1);
								}
							}
							sleepPlayer.setLastWarning((long) 0);
						}
						
					} else if(sleepPlayer.getLastRemind() > plugin.getConfig().getDouble("remind_delay")) {
						Long nextSleep = plugin.getConfig().getLong("sleep_delay") - sleepPlayer.getFromLastSleep();
						Bukkit.getPlayer(sleepPlayer.getName()).sendMessage(ChatColor.YELLOW + Translate.getString("Don't forget that you have to go sleep in next") + " " + ForceSleep.convertSeconds(nextSleep));
						sleepPlayer.setLastRemind(0L);
					}
				}
				SleepPlayers.updatePlayer(sleepPlayer);
				SleepPlayers.savePlayer(players[i].getName());
				sleepPlayer = null;
			}
		}
		
		// save new list
		this.players.clear();
		this.players = playersList;
	}

}
