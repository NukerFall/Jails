package me.NukerFall.Jails.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
			if (sender.hasPermission("jails.free")) {
				if (Bukkit.getPlayer(args[0]) != null) {
					UUID id = Bukkit.getPlayer(args[0]).getUniqueId();
					if (main.getJail().isJailed(id)) {
						main.getJail().free(id.toString());
						sender.sendMessage(Utils.clr(main.getLocale().getString("player-free")));
					} else {
						sender.sendMessage(Utils.clr(main.getLocale().getString("not-jailed")));
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("no-player")));
				}
			} else {
				sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
			}
		} else {
			sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
		}
		return true;
	}

}
