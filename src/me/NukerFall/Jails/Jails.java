package me.NukerFall.Jails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.google.common.base.Charsets;
import me.NukerFall.Jails.Commands.FCommand;
import me.NukerFall.Jails.Commands.JCommand;
import me.NukerFall.Jails.Commands.JSCommand;
import me.NukerFall.Jails.Listeners.Interact;
import me.NukerFall.Jails.Listeners.JoinEvent;
import me.NukerFall.Jails.Listeners.ToBlock;
import me.NukerFall.Jails.Utils.Utils;
import net.milkbowl.vault.economy.Economy;

public class Jails extends JavaPlugin {

	private File locale = new File(getDataFolder(), "locale.yml");
	FileConfiguration localeconf;
	Jail jail = new Jail(this);
	private static Economy econ;
	private ItemStack lockpick = new ItemStack(Material.valueOf(getConfig().getString("lockpick-material")), 1);
	BukkitRunnable br = new BukkitRunnable() {
		@Override
		public void run() {
			if (new File(getDataFolder(), "uuids").listFiles() != null) {
				for (File f : new File(getDataFolder(), "uuids").listFiles()) {
					FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
					String id = f.getName().replaceAll(".yml", "");
					if (conf.getInt("time") > 0) {
						conf.set("time", conf.getInt("time") - 1);
						try {
							conf.save(f);
						} catch (IOException e) {
							send("§cError with saving uuid file for " + id + "!");
							e.printStackTrace();
						}
						if (conf.getInt("time") == 0 && Bukkit.getOfflinePlayer(UUID.fromString(id)).isOnline()) {
							getJail().free(id);
						}
					}
				}
			}
		}
	};

	public void setLockPick() {
		ItemMeta meta = getLockpick().getItemMeta();
		meta.setDisplayName(Utils.clr(getConfig().getString("lockpick-name")));
		List<String> list = new ArrayList<String>();
		list.add(Utils.clr(getConfig().getString("lore-line")));
		meta.setLore(list);
		lockpick.setItemMeta(meta);
	}

	public ItemStack getLockpick() {
		return lockpick;
	}

	public void onEnable() {
		new File(getDataFolder() + File.separator + "jails").mkdir();
		new File(getDataFolder() + File.separator + "uuids").mkdir();
		if (getConfig().getBoolean("pay-exit")) {
			if (!setupEconomy()) {
				send("§cEconomy setup failed!");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
		saveDefaultConfig();
		saveDefaultLocale();
		localeconf = YamlConfiguration.loadConfiguration(locale);
		br.runTaskTimer(this, 1200L, 1200L);
		new JoinEvent(this);
		new FCommand(this);
		new JCommand(this);
		new JSCommand(this);
		new ToBlock(this);
		setLockPick();
		new Interact(this);
		send("§aPlugin enabled!");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private void saveDefaultLocale() {
		if (!locale.exists()) {
			saveResource("locale.yml", false);
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

	public Economy getEconomy() {
		return econ;
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
		localeconf.setDefaults(
				YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

}
