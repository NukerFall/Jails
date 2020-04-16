package me.NukerFall.Jails.Listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
						if (YamlConfiguration.loadConfiguration(f).getString("kept-id").equalsIgnoreCase(id.toString())) {
							f.delete();
						}
					}
				}
			} else if (!main.getJail().isInJail(id)) {
				main.getJail().jailOffline(id);
			}
		}
	}

}
