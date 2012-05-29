package com.gmail.wolinskip.forcesleep.data;

import org.bukkit.Bukkit;

public class SleepPlayer {
	private String 	name;
	private	Long 	fromLastSleep;
	private Long	totalOnline;
	private Long	lastWarning;
	private Long	lastRemind;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFromLastSleep(Long fromLastSleep) {
		this.fromLastSleep = fromLastSleep;
	}
	
	public void setTotalOnline(Long totalOnline) {
		this.totalOnline = totalOnline;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Long getFromLastSleep() {
		if(this.fromLastSleep == null || this.fromLastSleep.equals(null))
			return 0L;
		return new Long(this.fromLastSleep);
	}
	
	public Long getFirstOnline() {
		if(this.name != null && !this.name.equals(null))
			return Bukkit.getPlayer(this.name).getFirstPlayed();
		else
			return null;
	}
	
	public Long getTotalOnline() {
		if(this.totalOnline == null || this.totalOnline.equals(null))
			return 0L;
		return this.totalOnline;
	}

	public Long getLastWarning() {
		if(this.lastWarning.equals(null))
			this.setLastWarning(new Long(0));
		return this.lastWarning;
	}

	public void setLastWarning(Long lastWarning) {
		this.lastWarning = lastWarning;
	}

	public Long getLastRemind() {
		return lastRemind;
	}

	public void setLastRemind(Long lastRemind) {
		this.lastRemind = lastRemind;
	}
}
