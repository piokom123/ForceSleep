package com.gmail.wolinskip.forcesleep.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.wolinskip.forcesleep.ForceSleep;

public class SleepPlayers {
	private static ArrayList<SleepPlayer> players = new ArrayList<SleepPlayer>();
	private static Logger log = Logger.getLogger("Minecraft");
	
	/*
	 * loads player profile from file
	 */
	public static SleepPlayer loadPlayerByName(String name) {
		if(players.size() > 0) {
			for(int i=0;i<players.size();i++) {
				if(players.get(i).getName() != null && players.get(i).getName().equals(name)) {
					return players.get(i);
				}
			}
		}
		
		File playerFile = new File("plugins" + File.separator + ForceSleep.pluginName + File.separator + "players" + File.separator + "" + name + ".yml");
	    if(!playerFile.exists()) {
	    	try {
				playerFile.createNewFile();
			} catch (IOException e) {
				log.info("Can't create new player file!");
				e.printStackTrace();
				return null;
			}
	    }
	    YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerFile);
	    
	    SleepPlayer player = new SleepPlayer();

	    // set defaults
	    if(!playerYaml.contains("name")) {
	    	playerYaml.set("name", name);
	    }
	    if(!playerYaml.contains("from_last_sleep")) {
	    	playerYaml.set("from_last_sleep", null); // time from config
	    }
	    if(!playerYaml.contains("total_online")) {
	    	playerYaml.set("total_online", 0);
	    }
	    if(!playerYaml.contains("last_warning")) {
	    	playerYaml.set("last_warning", 0);
	    }
	    if(!playerYaml.contains("last_remind")) {
	    	playerYaml.set("last_remind", 0);
	    }
	    
	    // save player file
	    try {
		    playerYaml.save(playerFile);
		} catch (IOException e) {
			log.info("Can't save player file!");
			e.printStackTrace();
			return null;
		}
	    
	    player.setName(playerYaml.getString("name"));
	    player.setFromLastSleep(playerYaml.getLong("from_last_sleep"));
	    player.setTotalOnline(playerYaml.getLong("total_online"));
	    player.setLastWarning(playerYaml.getLong("last_warning"));
	    player.setLastRemind(playerYaml.getLong("last_remind"));
	    
	    players.add(player);
		    
		return player;
	}
	
	/*
	 * gets informations about player from memory
	 */
	public static SleepPlayer getPlayerByName(String name) {
		if(players.size() > 0) {
			for(int i=0;i<players.size();i++) {
				if(players.get(i).getName() != null && players.get(i).getName().equals(name)) {
					return players.get(i);
				}
			}
		}
		//return loadPlayerByName(name);
		return null;
	}
	
	/*
	 * adds player object to arraylist
	 */
	public static void addPlayer(SleepPlayer player) {
		if(SleepPlayers.getPlayerByName(player.getName()).equals(null)) {
			players.add(player);
		}
	}
	
	/*
	 * removes player from arraylist
	 */
	public static void removePlayer(String name) {
		if(players.size() > 0) {
			for(int i=0;i<players.size();i++) {
				if(players.get(i).getName().equals(name)) {
					players.remove(i);
					return;
				}
			}
		}
	}
	
	/*
	 * updates player
	 */
	public static void updatePlayer(SleepPlayer player) {
		removePlayer(player.getName());
		players.add(player);
	}
	
	/*
	 * saves players profile to file
	 */
	public static void savePlayer(String name) {
		SleepPlayer player = SleepPlayers.loadPlayerByName(name);
		if(player == null) {
			return;
		}
		
		File playerFile = new File("plugins" + File.separator + ForceSleep.pluginName + File.separator + "players" + File.separator + "" + player.getName() + ".yml");
	    if(!playerFile.exists()) {
	    	try {
				playerFile.createNewFile();
			} catch (IOException e) {
				log.info("Can't create new player file!");
				e.printStackTrace();
				return;
			}
	    }
	    YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerFile);
	    playerYaml.set("name", player.getName());
	    playerYaml.set("from_last_sleep", player.getFromLastSleep());
	    playerYaml.set("total_online", player.getTotalOnline());
	    playerYaml.set("last_warning", player.getLastWarning());
	    playerYaml.set("last_remind", player.getLastRemind());
	    try {
			playerYaml.save("plugins" + File.separator + "ForceSleep" + File.separator + "players" + File.separator + "" + player.getName() + ".yml");
		} catch (IOException e) {
			log.info("Can't save player file!");
			e.printStackTrace();
		}
	}
	
	/*
	 * saves all players to files
	 */
	public static void saveAllPlayers() {
		if(players.size() > 0) {
			for(int i=0;i<players.size();i++) {
				SleepPlayers.savePlayer(players.get(i).getName());
			}
		}
	}
	
	/*
	 * clears arraylist
	 */
	public static void clearList() {
		players.clear();
	}
}
