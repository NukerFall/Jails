package me.NukerFall.Jails.Listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.NukerFall.Jails.Jails;

public class JoinEvent implements Listener {
	
	private Jails main;
	public JoinEvent(Jails main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public void onJoin(PlayerJoinEvent e) {
		
	}

}
