package me.NukerFall.Jails.Listeners;

import java.util.Random;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class Interact implements Listener {

	private Jails main;

	public Interact(Jails main) {
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (main.getJail().isJailed(e.getPlayer().getUniqueId())) {
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (Tag.DOORS.isTagged(e.getClickedBlock().getType())) {
					if (!e.getHand().equals(EquipmentSlot.OFF_HAND)) {
						if (e.getItem() != null) {
							if (e.getItem().equals(main.getLockpick())) {
								e.setCancelled(true);
								e.getPlayer().getInventory().setItemInMainHand(null);
								Random r = new Random();
								Integer chance = r.nextInt(100);
								if (chance <= main.getConfig().getInt("lock-chance")) {
									e.getPlayer()
											.sendMessage(Utils.clr(main.getLocale().getString("lockpick-success")));
									BlockData s = e.getClickedBlock().getBlockData();
									Door d = (Door) s;
									d.setOpen(true);
									e.getClickedBlock().setBlockData(d);
									main.getJail().breakFree(e.getPlayer().getUniqueId());
									new BukkitRunnable() {
										public void run() {
											BlockData s = e.getClickedBlock().getBlockData();
											Door d = (Door) s;
											d.setOpen(false);
											e.getClickedBlock().setBlockData(d);
										}
									}.runTaskLater(this.main, 100L);
								} else {
									e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("lockpick-broken")));
									e.setCancelled(true);
								}
							} else {
								e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-interact")));
								e.setCancelled(true);
							}
						} else {
							e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-interact")));
							e.setCancelled(true);
						}
					} else {
						e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-interact")));
						e.setCancelled(true);
					}
				} else {
					e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-interact")));
					e.setCancelled(true);
				}
			} else {
				e.getPlayer().sendMessage(Utils.clr(main.getLocale().getString("no-interact")));
				e.setCancelled(true);
			}
		}
	}
}
