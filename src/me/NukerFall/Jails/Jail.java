package me.NukerFall.Jails;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.NukerFall.Jails.Utils.Utils;

public class Jail {

	private Jails main;

	public Jail(Jails main) {
		this.main = main;
	}

	public void free(String id) {
		File player = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		FileConfiguration pconf = YamlConfiguration.loadConfiguration(player);
		Bukkit.getPlayer(UUID.fromString(id)).teleport(pconf.getLocation("location"));
		Bukkit.getPlayer(UUID.fromString(id)).getInventory().clear();
		for (String s : pconf.getConfigurationSection("inventory").getKeys(false)) {
			Bukkit.getPlayer(UUID.fromString(id)).getInventory().setItem(Integer.valueOf(s), pconf.getItemStack("inventory." + s));
		}
		player.delete();
		for (File f : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(f);
			if (jconf.getBoolean("kept") && jconf.getString("kept-id").equalsIgnoreCase(id)) {
				f.delete();
				break;
			}
		}
		Bukkit.getPlayer(UUID.fromString(id)).sendMessage(Utils.clr(main.getLocale().getString("free-notification")));
	}

	public void jail(CommandSender sender, String id, Integer time, String reason, boolean online) {
		Boolean b = true;
		for (File jail : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(jail);
			if (!jconf.getBoolean("kept")) {
				jconf.set("kept", true);
				jconf.set("kept-id", id);
				try {
					jconf.save(jail);
				} catch (IOException ex) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("error-saving-jail")));
				}
				b = false;
				File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
				try {
					f.createNewFile();
				} catch (IOException e) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("error-jailing")));
					return;
				}
				FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
				conf.set("time", time);
				conf.set("reason", reason);
				if (online) {
					for (Integer i = 0; i < Bukkit.getPlayer(id).getInventory().getSize(); i++) {
						if (Bukkit.getPlayer(id).getInventory().getItem(i) != null) {
							conf.set("inventory." + i, Bukkit.getPlayer(id).getInventory().getItem(i));
						}
					}
				}
				try {
					conf.save(f);
				} catch (IOException e) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("error-saving-player")));
				}
				break;
			}
		}
		if (b) {
			sender.sendMessage(Utils.clr(main.getConfig().getString("no-empty-jails")));
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
		File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		for (Integer i = 0; i < Bukkit.getPlayer(id).getInventory().getSize(); i++) {
			if (Bukkit.getPlayer(id).getInventory().getItem(i) != null) {
				conf.set("inventory." + i, Bukkit.getPlayer(id).getInventory().getItem(i));
			}
		}
		String name = "";
		for (File jail : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(jail);
			if (jconf.getString("kept-id").equalsIgnoreCase(id.toString())) {
				name = jail.getName().replaceAll(".yml", "");
				break;
			}
		}
		Bukkit.getPlayer(id).getInventory().clear();
		Bukkit.getPlayer(id).teleport(getJailLocation(name));
	}

	private Location getJailLocation(String name) {
		File jail = new File(main.getDataFolder() + File.separator + "jails", name + ".yml");
		return YamlConfiguration.loadConfiguration(jail).getLocation("location");
	}

	public void setPlayerTime(CommandSender sender, UUID id, int i) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		conf.set("time", 0);
		try {
			conf.save(f);
		} catch (IOException e) {
			sender.sendMessage(Utils.clr(main.getLocale().getString("error-saving-player")));
		}
	}

	public boolean isKept(String string) {
		File f = new File(main.getDataFolder() + File.separator + "jails", string + ".yml");
		return YamlConfiguration.loadConfiguration(f).getBoolean("kept");
	}

	public String getPlayerJail(String string) {
		for (File jail : new File(main.getDataFolder(), "jails").listFiles()) {
			if (YamlConfiguration.loadConfiguration(jail).getString("kept-id").equalsIgnoreCase(Bukkit.getPlayer(string).getUniqueId().toString())) {
				return jail.getName().replaceAll(".yml", "");
			}
		}
		return null;
	}

}
