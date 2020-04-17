package me.NukerFall.Jails.Commands;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class JCommand implements CommandExecutor {

	private Jails main;

	public JCommand(Jails main) {
		this.main = main;
		main.getCommand("jail").setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 2) {
				if (sender.hasPermission("jails.jail")) {
					if (Bukkit.getOfflinePlayer(args[0]) != null) {
						UUID id = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
						if (!sender.getName().equalsIgnoreCase(args[0])) {
							if (!main.getJail().isJailed(id)) {
								Integer time = 0;
								try {
									time = Integer.parseInt(args[1]);
								} catch (NumberFormatException ex) {
									sender.sendMessage(Utils.clr(main.getLocale().getString("number-exception")));
									return false;
								}
								Location loc = Bukkit.getWorlds().get(0).getSpawnLocation();
								switch (main.getConfig().getString("free-location")) {
								case "LAST_LOCATION":
									if (Bukkit.getOfflinePlayer(id).isOnline()) {
										loc = Bukkit.getPlayer(id).getLocation();
									}
									break;
								case "JAILS_LOCATION":
									if (main.getConfig().getConfigurationSection("location") != null) {
										loc = main.getConfig().getLocation("location");
									}
									break;
								case "SPAWN_LOCATION":
									if (sender instanceof Player) {
										loc = ((Player) sender).getWorld().getSpawnLocation();
									}
									break;
								default:
									if (sender instanceof Player) {
										loc = ((Player) sender).getWorld().getSpawnLocation();
									}
									sender.sendMessage(Utils.clr(main.getLocale().getString("world-error")));
									break;
								}
								if (main.getJail().jail((Player) sender, id.toString(), time,
										main.getConfig().getString("no-reason"), Bukkit.getOfflinePlayer(id).isOnline(),
										loc)) {
									Bukkit.broadcastMessage(Utils.clr(main.getLocale().getString("jail-broadcast")
											.replaceAll("%name%", args[0]).replaceAll("%time%", time.toString())
											.replaceAll("%reason%", main.getConfig().getString("no-reason"))));
								}
							} else {
								sender.sendMessage(Utils.clr(main.getLocale().getString("already-jailed")));
							}
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("no-self-jail")));
						}
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
				}
			} else if (args.length >= 3) {
				if (sender.hasPermission("jails.jail")) {
					if (Bukkit.getPlayer(args[0]).getUniqueId() != null) {
						UUID id = Bukkit.getPlayer(args[0]).getUniqueId();
						if (!sender.getName().equalsIgnoreCase(args[0])) {
							if (!main.getJail().isJailed(id)) {
								Integer time = 0;
								Location loc = Bukkit.getWorlds().get(0).getSpawnLocation();
								switch (main.getConfig().getString("free-location")) {
								case "LAST_LOCATION":
									if (Bukkit.getOfflinePlayer(id).isOnline()) {
										loc = Bukkit.getPlayer(id).getLocation();
									}
									break;
								case "JAILS_LOCATION":
									if (main.getConfig().getConfigurationSection("location") != null) {
										loc = main.getConfig().getLocation("location");
									}
									break;
								case "SPAWN_LOCATION":
									if (sender instanceof Player) {
										loc = ((Player) sender).getWorld().getSpawnLocation();
									}
									break;
								default:
									if (sender instanceof Player) {
										loc = ((Player) sender).getWorld().getSpawnLocation();
									}
									sender.sendMessage(Utils.clr(main.getLocale().getString("world-error")));
									break;
								}
								StringBuilder reason = new StringBuilder();
								for (Integer i = 2; i < args.length; i++) {
									reason.append(" " + args[i]);
								}
								try {
									time = Integer.parseInt(args[1]);
								} catch (NumberFormatException ex) {
									sender.sendMessage(Utils.clr(main.getLocale().getString("number-exception")));
								}
								if (main.getJail().jail((Player) sender, id.toString(), time,
										reason.toString().replaceFirst(" ", ""), Bukkit.getOfflinePlayer(id).isOnline(),
										loc)) {
									Bukkit.broadcastMessage(Utils.clr(main.getLocale().getString("jail-broadcast")
											.replaceAll("%name%", args[0]).replaceAll("%time%", time.toString())
											.replaceAll("%reason%", reason.toString().replaceFirst(" ", ""))));
								}
							} else {
								sender.sendMessage(Utils.clr(main.getLocale().getString("already-jailed")));
							}
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("no-self-jail")));
						}
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
				}
			} else {
				sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
			}
		} else {
			sender.sendMessage(Utils.clr(main.getLocale().getString("only-players")));
		}
		return true;
	}
}
