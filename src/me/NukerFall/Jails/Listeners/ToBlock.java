package me.NukerFall.Jails.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			if (main.getConfig().getBoolean("disable-chat")) {
				e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-chat")));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (main.getJail().isJailed(((Player) e.getWhoClicked()).getUniqueId())) {
			e.getWhoClicked().sendMessage(Utils.clr(main.getLocale().getString("no-inventory")));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (main.getJail().isJailed(((Player) e.getWhoClicked()).getUniqueId())) {
			e.getWhoClicked().sendMessage(Utils.clr(main.getLocale().getString("no-inventory")));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-drop")));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-break")));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-place")));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			String pjail = main.getJail().getPlayerJail(e.getPlayer().getName());
			Location loc = main.getJail().getJailLocation(pjail);
			e.setRespawnLocation(loc);
			if (main.getConfig().getBoolean("set-invulnerable")) {
				e.getPlayer().setInvulnerable(true);
			}
			if (main.getConfig().getBoolean("change-gamemode")) {
				e.getPlayer().setGameMode(GameMode.SURVIVAL);
				e.getPlayer().setFlying(false);
			}
		}
	}

}
