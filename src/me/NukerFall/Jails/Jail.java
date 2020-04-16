package me.NukerFall.Jails;

import java.io.File;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class Jail {

	private Jails main;

	public Jail(Jails main) {
		this.main = main;
	}

	public void free(String id) {
		
	}
	
	public void freeOffline(String id) {
		
	}

	public void jail(CommandSender sender, String id, Integer time, String reason, boolean online) {
		if (online) {
			
		} else {
			
		}
	}

	public Integer getPlayerTime(UUID id) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		return YamlConfiguration.loadConfiguration(f).getInt("time");
	}

	public boolean isJailed(UUID id) {
		if (new File(main.getDataFolder() + File.separator + "uuids", id + ".yml").exists()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInJail(UUID id) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		if (YamlConfiguration.loadConfiguration(f).getConfigurationSection("inventory") != null) {
			return true;
		} else {
			return false;
		}
	}

	public void jailOffline(UUID id) {
		
	}

}
