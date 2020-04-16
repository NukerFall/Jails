package me.NukerFall.Jails.Listeners;

import java.util.UUID;
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
				main.getJail().free(id.toString());
			} else if (!main.getJail().isInJail(id)) {
				main.getJail().jailOffline(id);
			}
		}
	}

}
