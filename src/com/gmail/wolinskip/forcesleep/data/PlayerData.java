package com.gmail.wolinskip.forcesleep.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	private Logger log = Logger.getLogger("Minecraft");
	
	public SleepPlayer getPlayerByName(String name) {
	    File playerFile = new File("plugins" + File.separator + "ForceSleep" + File.separator + "players" + File.separator + "" + name + ".yml");
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
	    //log.info(this.formatter.format(new Date()));
	    // set defaults
	    if(!playerYaml.contains("name")) {
	    	playerYaml.set("name", name);
	    }
	    if(!playerYaml.contains("first_online")) {
	    	playerYaml.set("first_online", System.currentTimeMillis()/1000);
	    }
	    if(!playerYaml.contains("from_last_sleep")) {
	    	playerYaml.set("last_sleep", 0);
	    }
	    if(!playerYaml.contains("total_online")) {
	    	playerYaml.set("total_online", 0);
	    }
	    if(!playerYaml.contains("last_warning")) {
	    	playerYaml.set("last_warning", 0);
	    }
	    
	    player.setName(playerYaml.getString("name"));
		//log.info(playerYaml.get("first_online").toString());
		//Date firstOnline = this.formatter.parse(playerYaml.getString("first_online"));
	    //player.setFirstOnline(playerYaml.getLong("first_online"));
	    
	    //Date lastSleep = this.formatter.parse(playerYaml.getString("last_sleep")); 
	    player.setFromLastSleep(playerYaml.getLong("from_last_sleep"));
	    player.setTotalOnline(playerYaml.getLong("total_online"));
	    player.setLastWarning(playerYaml.getLong("last_warning"));
		    
		return player;
	}
	
	public void savePlayer(SleepPlayer player) {
		File playerFile = new File("plugins" + File.separator + "ForceSleep" + File.separator + "players" + File.separator + "" + player.getName() + ".yml");
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
	    playerYaml.set("first_online", player.getFirstOnline());
	    playerYaml.set("from_last_sleep", player.getFromLastSleep());
	    playerYaml.set("total_online", player.getTotalOnline());
	    playerYaml.set("last_warning", player.getLastWarning());
	    try {
			playerYaml.save("plugins" + File.separator + "ForceSleep" + File.separator + "players" + File.separator + "" + player.getName() + ".yml");
		} catch (IOException e) {
			log.info("Can't save player file!");
			e.printStackTrace();
		}
	}
}
