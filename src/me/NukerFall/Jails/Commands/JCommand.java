package me.NukerFall.Jails.Commands;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class JCommand implements CommandExecutor {
	
	private Jails main;
	public JCommand(Jails main) {
		this.main = main;
		main.getCommand("jail").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (Bukkit.getPlayer(args[0]).getUniqueId() != null) {
				UUID id = Bukkit.getPlayer(args[0]).getUniqueId();
				Integer time = 0;
				try {
					Integer.parseInt(args[1]);
				} catch (NumberFormatException ex) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("number-exception")));
				}
				main.getJail().jail(sender, id.toString(), time, main.getConfig().getString("no-reason"),
						Bukkit.getOfflinePlayer(id).isOnline());
				Bukkit.broadcastMessage(Utils.clr(main.getLocale().getString("jail-broadcast").replaceAll("%name%", args[0])
						.replaceAll("%time%", time.toString()).replaceAll("%reason%", main.getConfig().getString("no-reason"))));
			}
		} else if (args.length >= 3) {
			if (Bukkit.getPlayer(args[0]).getUniqueId() != null) {
				UUID id = Bukkit.getPlayer(args[0]).getUniqueId();
				Integer time = 0;
				StringBuilder reason = new StringBuilder();
				for (Integer i = 2; i < args.length; i++) {
					reason.append(" " + args[i]);
				}
				try {
					Integer.parseInt(args[1]);
				} catch (NumberFormatException ex) {
					sender.sendMessage(Utils.clr(main.getLocale().getString("number-exception")));
				}
				main.getJail().jail(sender, id.toString(), time, reason.toString().replaceFirst(" ", ""),
						Bukkit.getOfflinePlayer(id).isOnline());
				Bukkit.broadcastMessage(Utils.clr(main.getLocale().getString("jail-broadcast").replaceAll("%name%", args[0])
				.replaceAll("%time%", time.toString()).replaceAll("%reason%", reason.toString().replaceFirst(" ", ""))));
			}
		} else {
			sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
		}
		return true;
	}

}
