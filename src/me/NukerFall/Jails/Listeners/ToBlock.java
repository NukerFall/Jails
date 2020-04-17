package me.NukerFall.Jails.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class ToBlock implements Listener {

	private Jails main;

	public ToBlock(Jails main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if (e.getEntity().getType().equals(EntityType.PLAYER)) {
			Player p = (Player) e.getEntity();
			if (main.getJail().isJailed(p.getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			if (!e.getMessage().split(" ")[0].equalsIgnoreCase("/free")) {
				e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-commands")));
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if (main.getJail().isInJail(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-teleport")));
			e.setCancelled(true);
		}
	}

}
