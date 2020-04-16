package me.NukerFall.Jails;

import org.bukkit.plugin.java.JavaPlugin;

public class Jails extends JavaPlugin {
	
	public void onEnable() {
		send("§aPlugin enabled!");
	}
	
	public void onDisable() {
		send("§cPlugin disabled!");
	}
	
	private void send(String s) {
		getServer().getConsoleSender().sendMessage("[Jails] " + s);
	}

}
