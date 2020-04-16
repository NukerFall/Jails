package me.NukerFall.Jails.Commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class JSCommand implements CommandExecutor {

	private Jails main;

	public JSCommand(Jails main) {
		this.main = main;
		main.getCommand("jails").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("jails.admin")) {
				if (args.length == 1) {
					switch (args[0]) {
					case "help":
						throwHelp(sender, true);
						break;
					case "info":
						throwInfo(sender);
						break;
					case "reload":
						main.reloadConfig();
						main.reloadLocale();
						sender.sendMessage(Utils.clr(main.getLocale().getString("reloaded")));
						break;
					case "list":
						break;
					case "playerlist":
						break;
					default:
						sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
						break;
					}
				} else if (args.length == 2) {
					switch (args[0]) {
					case "jail":
						File jail = new File(main.getDataFolder() + File.separator + "jails", args[1] + ".yml");
						if (jail.exists()) {
							
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-not-exist")));
						}
						break;
					case "player":
						break;
					case "create":
						File created = new File(main.getDataFolder() + File.separator + "jails", args[1] + ".yml");
						if (created.exists()) {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-already-exist")));
						} else {

						}
						break;
					case "remove":
						File removed = new File(main.getDataFolder() + File.separator + "jails", args[1] + ".yml");
						if (removed.exists()) {

						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-not-exist")));
						}
						break;
					default:
						sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
						break;
					}
				} else if (args.length == 0) {
					if (sender.hasPermission("jails.admin")) {
						throwInfo(sender);
					} else {
						sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
					}
				} else {
					sender.sendMessage(Utils.clr(main.getLocale().getString("args")));
				}
			} else if (args[0].equalsIgnoreCase("help")) {
				throwHelp(sender, false);
			} else {
				sender.sendMessage(Utils.clr(main.getLocale().getString("no-perm")));
			}
		} else {
			sender.sendMessage(Utils.clr(main.getLocale().getString("only-players")));
		}
		return true;
	}

	private void throwInfo(CommandSender sender) {
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
	}

	private void throwHelp(CommandSender sender, boolean permit) {
		sender.sendMessage(Utils.clr(""));
		sender.sendMessage(Utils.clr(""));
		if (permit) {
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
			sender.sendMessage(Utils.clr(""));
		}
	}

}
