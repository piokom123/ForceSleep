package com.gmail.wolinskip.forcesleep.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.wolinskip.forcesleep.ForceSleep;
import com.gmail.wolinskip.forcesleep.Translate;
import com.gmail.wolinskip.forcesleep.data.SleepPlayer;
import com.gmail.wolinskip.forcesleep.data.SleepPlayers;

public class UserCommands implements CommandExecutor{
	private ForceSleep plugin = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

	/*
	 * saves instance of plugin
	 */
	public UserCommands(ForceSleep forceSleep) {
		this.plugin = forceSleep;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		if(player == null) {
			sender.sendMessage(Translate.getString("Command can be run only by player."));
			return true;
		}
		
		if(args.length == 0) {
			return false;
		}
		
		if(args[0].equalsIgnoreCase("info")) {
			//PlayerData data = new PlayerData();
			SleepPlayer sleepPlayer = SleepPlayers.loadPlayerByName(player.getName());
			if(sleepPlayer == null) {
				sender.sendMessage(ChatColor.RED + Translate.getString("Error occured while trying to show info. Sorry."));
				return true;
			}
			sender.sendMessage(Translate.getString("First time online:") + " " + formatter.format(new Date(sleepPlayer.getFirstOnline())));
			sender.sendMessage(Translate.getString("Time online:") + " " + ForceSleep.convertSeconds(sleepPlayer.getTotalOnline()));
			
			// time from last sleep
			sender.sendMessage(Translate.getString("Time from last sleep:") + " " + ForceSleep.convertSeconds(sleepPlayer.getFromLastSleep()));
			
			// next sleep
			Long next_sleep = plugin.getConfig().getLong("sleep_delay") - sleepPlayer.getFromLastSleep();
			if(next_sleep > 0)
				sender.sendMessage(Translate.getString("You have to go to sleep in next") + " " + ForceSleep.convertSeconds(next_sleep));
			else
				sender.sendMessage(ChatColor.RED + Translate.getString("You have to go to sleep as fast as possible!"));
				
			return true;
		}
		
		return false;
	}
}
