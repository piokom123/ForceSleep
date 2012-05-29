package com.gmail.wolinskip.forcesleep;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.wolinskip.forcesleep.commands.UserCommands;
import com.gmail.wolinskip.forcesleep.listeners.SleepPlayerListener;
import com.gmail.wolinskip.forcesleep.Metrics;
import com.gmail.wolinskip.forcesleep.UpdateChecker;

public class ForceSleep extends JavaPlugin {
	
	Logger log	= Logger.getLogger("Minecraft");
	private UserCommands userCommands;
	private FileConfiguration config;
	public static String pluginName = "ForceSleep";
	
	/*
	 * runs when plugin is starting
	 */
	@Override
	public void onEnable() {
		try {
			// check if plugin is up to date
			UpdateChecker.checkForUpdate(pluginName);
			
			// start PluginMetrics
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();
			} catch (IOException e) {
			    log.warning("Error occured while starting PluginMetrics");
			}

			// get config file
			config = getConfig();
			File configFile = new File("plugins" + File.separator + pluginName + File.separator + "players");
			configFile.mkdirs();
			
			configFile = new File("plugins" + File.separator + pluginName + File.separator + "config.yml");
			if(!configFile.exists()) {
				configFile.createNewFile();
			}
			
			// set default values
			if(!config.contains("world")) {
				config.set("world", "world");
			}
			
			if(!config.contains("refresh_time")) {
				config.set("refresh_time", 30);
			}
			
			if(!config.contains("sleep_delay")) {
				config.set("sleep_delay", 12 * 60);
			}
			
			if(!config.contains("warning_delay")) {
				config.set("warning_delay", 15);
			}
			
			if(!config.contains("minimum_health.day")) {
				config.set("minimum_health.day", 15);
			}
			
			if(!config.contains("minimum_health.night")) {
				config.set("minimum_health.night", 2);
			}

			// save updated config file
			saveConfig();
			
			// loads translation
			if(config.contains("language")) {
				Translate.setLang(config.getString("language"));
			}
			Translate.loadTranslations(pluginName);
			
			// register player listener event
			final SleepPlayerListener sleepPlayerListener = new SleepPlayerListener(this);
			getServer().getPluginManager().registerEvents(sleepPlayerListener, this);
			
			// sets executor for basic user command /forcesleep
			userCommands = new UserCommands(this);
			getCommand("forcesleep").setExecutor(userCommands);
			
			// set scheduler task
			Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			    public void run() {
			    	sleepPlayerListener.changeOnlineTime();
			    }
			}, 40L, config.getLong("refresh_time")*20);
		
		} catch(Exception e1) {
			log.info("Error while loading plugin!");
			e1.printStackTrace();
			return;
		}
		
		log.info("ForceSleep enabled.");
	}
	
	/*
	 * runs when plugin is exiting
	 */
	@Override
	public void onDisable() {
		Translate.saveTranslations(pluginName);
		log.info("ForceSleep disabled.");
	}
	
	/*
	 * formats seconds to string with minutes and seconds
	 */
	public static String convertSeconds(Long seconds) {
		if(seconds < 60) {
			return seconds + " seconds";
		}
		Long minutes = (long) Math.floor(seconds / 60);
		String result = Long.toString(minutes) + " minutes";
		seconds = seconds - (minutes * 60);
		if(seconds > 0) {
			result = result + " " + Long.toString(seconds) + " seconds";
		}
		return result;
	}
}

