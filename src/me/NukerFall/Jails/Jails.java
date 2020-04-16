package me.NukerFall.Jails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.google.common.base.Charsets;

public class Jails extends JavaPlugin {

	private File locale = new File(getDataFolder(), "locale.yml");
	FileConfiguration localeconf;
	Jail jail = new Jail(this);
	BukkitRunnable br = new BukkitRunnable() {
		@Override
		public void run() {
			for (File f : new File(getDataFolder() + File.separator + "uuids").listFiles()) {
				FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
				String id = f.getName().replaceAll(".yml", "");
				if (conf.getInt("time") > 0) {
					conf.set("time", conf.getInt("time") - 1);
					try {
						conf.save(f);
					} catch (IOException e) {
						send("§cError with saving uuid file for " + id + "!");
					}
					if (conf.getInt("time") == 0 && Bukkit.getOfflinePlayer(UUID.fromString(id)).isOnline()) {
						getJail().free(id);
					}
				}
			}
		}
	};

	public void onEnable() {
		localeconf = YamlConfiguration.loadConfiguration(locale);
		saveDefaultConfig();
		saveDefaultLocale();
		br.runTaskTimerAsynchronously(this, 1200L, 1200L);
		send("§aPlugin enabled!");
	}

	private void saveDefaultLocale() {
		if (!locale.exists()) {
			try {
				locale.createNewFile();
			} catch (IOException e) {
				send("§cError with saving locale!");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

	public void onDisable() {
		send("§cPlugin disabled!");
	}

	private void send(String s) {
		getServer().getConsoleSender().sendMessage("[Jails] " + s);
	}

	public Jail getJail() {
		return jail;
	}

	public FileConfiguration getLocale() {
		return localeconf;
	}

	public void reloadLocale() {
		localeconf = YamlConfiguration.loadConfiguration(locale);
	    InputStream defConfigStream = getResource("locale.yml");
	    if (defConfigStream == null) {
	      return;
	    }
	    localeconf.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

}
