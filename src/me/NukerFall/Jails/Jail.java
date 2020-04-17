package me.NukerFall.Jails;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
		if (pconf.getConfigurationSection("inventory").getKeys(false).size() != 0) {
			for (String s : pconf.getConfigurationSection("inventory").getKeys(false)) {
				Bukkit.getPlayer(UUID.fromString(id)).getInventory().setItem(Integer.valueOf(s),
						pconf.getItemStack("inventory." + s));
			}
		}
		player.delete();
		for (File f : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(f);
			if (jconf.getBoolean("kept") && jconf.getString("kept-id").equalsIgnoreCase(id)) {
				jconf.set("kept", false);
				jconf.set("kept-id", null);
				try {
					jconf.save(f);
				} catch (IOException ex) {
					for (Player pl : Bukkit.getOnlinePlayers()) {
						if (pl.isOp()) {
							pl.sendMessage(Utils.clr(main.getLocale().getString("error-saving-jail")));
						}
					}
				}
				break;
			}
		}
		Bukkit.getPlayer(UUID.fromString(id)).sendMessage(Utils.clr(main.getLocale().getString("free-notification")));
	}

	public boolean jail(CommandSender sender, String id, Integer time, String reason, boolean online, Location loc) {
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
					return false;
				}
				b = false;
				Bukkit.getPlayer(UUID.fromString(id)).teleport(jconf.getLocation("location"));
				File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
				try {
					f.createNewFile();
				} catch (IOException e) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("error-jailing")));
					return false;
				}
				FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
				conf.set("time", time);
				conf.set("reason", reason);
				conf.set("location", loc);
				if (online) {
					Boolean empty = true;
					for (Integer i = 0; i < Bukkit.getPlayer(UUID.fromString(id)).getInventory().getSize(); i++) {
						if (Bukkit.getPlayer(UUID.fromString(id)).getInventory().getItem(i) != null) {
							conf.set("inventory." + i, Bukkit.getPlayer(UUID.fromString(id)).getInventory().getItem(i));
							empty = false;
						}
					}
					if (empty) {
						conf.createSection("inventory");
					}
					Bukkit.getPlayer(UUID.fromString(id)).getInventory().clear();
					Bukkit.getPlayer(UUID.fromString(id))
							.sendMessage(Utils.clr(main.getLocale().getString("jailed-notification")));
					Bukkit.getPlayer(UUID.fromString(id)).getInventory().setItemInMainHand(main.getLockpick());
					if (main.getConfig().getBoolean("change-gamemode")) {
						Bukkit.getPlayer(UUID.fromString(id)).setGameMode(GameMode.SURVIVAL);
					}
				}
				try {
					conf.save(f);
				} catch (IOException e) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("error-saving-player")));
					e.printStackTrace();
					return false;
				}
				break;
			}
		}
		if (b) {
			sender.sendMessage(Utils.clr(main.getLocale().getString("no-empty-jails")));
			return false;
		} else {
			return true;
		}
	}

	public Integer getPlayerTime(UUID id) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id.toString() + ".yml");
		return YamlConfiguration.loadConfiguration(f).getInt("time");
	}

	public boolean isJailed(UUID id) {
		if (new File(main.getDataFolder() + File.separator + "uuids", id.toString() + ".yml").exists()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInJail(UUID id) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id.toString() + ".yml");
		if (YamlConfiguration.loadConfiguration(f).getConfigurationSection("inventory") != null) {
			return true;
		} else {
			return false;
		}
	}

	public void jailOffline(UUID id) {
		File f = new File(main.getDataFolder() + File.separator + "uuids", id + ".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		Boolean empty = true;
		for (Integer i = 0; i < Bukkit.getPlayer(id).getInventory().getSize(); i++) {
			if (Bukkit.getPlayer(id).getInventory().getItem(i) != null) {
				conf.set("inventory." + i, Bukkit.getPlayer(id).getInventory().getItem(i));
				empty = false;
			}
		}
		if (empty) {
			conf.createSection("inventory");
		}
		String name = "";
		for (File jail : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(jail);
			if (jconf.getString("kept-id").equalsIgnoreCase(id.toString())) {
				name = jail.getName().replaceAll(".yml", "");
				break;
			}
		}
		try {
			conf.save(f);
		} catch (IOException ex) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (pl.isOp()) {
					pl.sendMessage(Utils.clr(main.getLocale().getString("error-saving-jail")));
				}
			}
		}
		Bukkit.getPlayer(id).getInventory().clear();
		Bukkit.getPlayer(id).teleport(getJailLocation(name));
		Bukkit.getPlayer(id).sendMessage(Utils.clr(main.getLocale().getString("jailed-notification")));
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
			e.printStackTrace();
		}
	}

	public boolean isKept(String string) {
		File f = new File(main.getDataFolder() + File.separator + "jails", string + ".yml");
		return YamlConfiguration.loadConfiguration(f).getBoolean("kept");
	}

	public String getPlayerJail(String string) {
		for (File jail : new File(main.getDataFolder(), "jails").listFiles()) {
			if (YamlConfiguration.loadConfiguration(jail).getString("kept-id")
					.equalsIgnoreCase(Bukkit.getPlayer(string).getUniqueId().toString())) {
				return jail.getName().replaceAll(".yml", "");
			}
		}
		return null;
	}
	

	public void breakFree(UUID id) {
		File player = new File(main.getDataFolder() + File.separator + "uuids", id.toString() + ".yml");
		FileConfiguration pconf = YamlConfiguration.loadConfiguration(player);
		if (pconf.getConfigurationSection("inventory").getKeys(false).size() != 0) {
			for (String s : pconf.getConfigurationSection("inventory").getKeys(false)) {
				Bukkit.getPlayer(id).getInventory().setItem(Integer.valueOf(s),
						pconf.getItemStack("inventory." + s));
			}
		}
		player.delete();
		for (File f : new File(main.getDataFolder(), "jails").listFiles()) {
			FileConfiguration jconf = YamlConfiguration.loadConfiguration(f);
			if (jconf.getBoolean("kept") && jconf.getString("kept-id").equalsIgnoreCase(id.toString())) {
				jconf.set("kept", false);
				jconf.set("kept-id", null);
				try {
					jconf.save(f);
				} catch (IOException ex) {
					for (Player pl : Bukkit.getOnlinePlayers()) {
						if (pl.isOp()) {
							pl.sendMessage(Utils.clr(main.getLocale().getString("error-saving-jail")));
						}
					}
				}
				break;
			}
		}
	}

}
