package me.NukerFall.Jails.Listeners;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class JoinEvent implements Listener {

	private Jails main;

	public JoinEvent(Jails main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID id = p.getUniqueId();
		if (main.getJail().isJailed(id)) {
			if (main.getJail().getPlayerTime(id) == 0) {
				if (main.getJail().isInJail(id)) {
					main.getJail().free(id.toString());
				} else {
					File player = new File(main.getDataFolder() + File.separator + "uuids", id.toString() + ".yml");
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
						}
					}
				}
			} else if (!main.getJail().isInJail(id)) {
				main.getJail().jailOffline(id);
			}
		}
	}

}
