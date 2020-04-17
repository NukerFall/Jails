package me.NukerFall.Jails.Commands;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class FCommand implements CommandExecutor {

	private Jails main;

	public FCommand(Jails main) {
		this.main = main;
		main.getCommand("free").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (sender.hasPermission("jails.admin")) {
				if (Bukkit.getPlayer(args[0]) != null) {
					UUID id = Bukkit.getPlayer(args[0]).getUniqueId();
					if (main.getJail().isJailed(id)) {
						if (Bukkit.getOfflinePlayer(id).isOnline()) {
							main.getJail().free(id.toString());
						} else {
							main.getJail().setPlayerTime(sender, id, 0);
						}
						sender.sendMessage(Utils.clr(main.getLocale().getString("player-free")));
					} else {
						sender.sendMessage(Utils.clr(main.getLocale().getString("player-not-jailed")));
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("no-player")));
				}
			} else {
				sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
			}
		} else if (args.length == 0) {
			if (sender instanceof Player) {
				if (sender.hasPermission("jails.free")) {
					if (main.getJail().isJailed(((Player) sender).getUniqueId())) {
						if (main.getConfig().getBoolean("pay-exit")) {
							if (main.getEconomy() != null) {
								Double cost = main.getConfig().getDouble("cost-per-min")
										* main.getJail().getPlayerTime(((Player) sender).getUniqueId());
								if (main.getEconomy()
										.getBalance(Bukkit.getOfflinePlayer(((Player) sender).getUniqueId())) >= cost) {
									main.getEconomy().withdrawPlayer(
											Bukkit.getOfflinePlayer(((Player) sender).getUniqueId()), cost);
									main.getJail().free(((Player) sender).getUniqueId().toString());
								} else {
									sender.sendMessage(Utils.clr(main.getLocale().getString("no-money")));
								}
							} else {
								sender.sendMessage(Utils.clr(main.getLocale().getString("no-eco")));
							}
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("pay-exit-disabled")));
						}
					} else {
						sender.sendMessage(Utils.clr(main.getLocale().getString("player-not-jailed")));
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
				}
			} else {
				sender.sendMessage(Utils.clr(main.getLocale().getString("only-players")));
			}
		} else {
			sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
		}
		return true;
	}

}
