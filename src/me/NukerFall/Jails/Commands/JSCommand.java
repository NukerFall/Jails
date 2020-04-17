package me.NukerFall.Jails.Commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.NukerFall.Jails.Jails;
import me.NukerFall.Jails.Utils.Utils;

public class JSCommand implements CommandExecutor {

	private Jails main;

	public JSCommand(Jails main) {
		this.main = main;
		main.getCommand("jails").setExecutor(this);
	}

	@SuppressWarnings("deprecation")
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
					case "setspawn":
						main.getConfig().set("location", ((Player) sender).getLocation());
						sender.sendMessage(Utils.clr(main.getLocale().getString("spawn-set")));
						break;
					case "list":
						if (new File(main.getDataFolder() + File.separator + "jails").listFiles().length != 0) {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jails-title")));
							for (File f : new File(main.getDataFolder() + File.separator + "jails").listFiles()) {
								sender.sendMessage(Utils.clr(main.getLocale().getString("jail-format")
										.replaceAll("%name%", f.getName().replaceAll(".yml", ""))
										.replaceAll("%kept%", String
												.valueOf(YamlConfiguration.loadConfiguration(f).getBoolean("kept")))));
							}
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("no-jails")));
						}
						break;
					case "playerlist":
						if (new File(main.getDataFolder() + File.separator + "uuids").listFiles().length != 0) {
							sender.sendMessage(Utils.clr(main.getLocale().getString("players-title")));
							for (File f : new File(main.getDataFolder() + File.separator + "uuids").listFiles()) {
								sender.sendMessage(Utils.clr(main.getLocale().getString("player-format")
										.replaceAll("%name%",
												Bukkit.getOfflinePlayer(
														UUID.fromString(f.getName().replaceAll(".yml", ""))).getName())
										.replaceAll("%jailname%", main.getJail().getPlayerJail(Bukkit.getOfflinePlayer(
												UUID.fromString(f.getName().replaceAll(".yml", ""))).getName()))));
							}
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("no-players")));
						}
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
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-format")
									.replaceAll("%name%", args[1]).replaceAll("%kept%", String
											.valueOf(YamlConfiguration.loadConfiguration(jail).getBoolean("kept")))));
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-not-exist")));
						}
						break;
					case "player":
						File player = new File(main.getDataFolder() + File.separator + "uuids",
								Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString() + ".yml");
						if (player.exists()) {
							sender.sendMessage(
									Utils.clr(main.getLocale().getString("player-format").replaceAll("%name%", args[1])
											.replaceAll("%jailname%", main.getJail().getPlayerJail(args[1]))));
						} else {
							sender.sendMessage(Utils.clr(main.getLocale().getString("player-not-jailed")));
						}
						break;
					case "create":
						File created = new File(main.getDataFolder() + File.separator + "jails", args[1] + ".yml");
						if (created.exists()) {
							sender.sendMessage(Utils.clr(main.getLocale().getString("jail-already-exist")));
						} else {
							try {
								created.createNewFile();
								FileConfiguration conf = YamlConfiguration.loadConfiguration(created);
								conf.set("kept", false);
								conf.set("location", ((Player) sender).getLocation());
								conf.save(created);
								sender.sendMessage(Utils.clr(main.getLocale().getString("created")));
							} catch (IOException e) {
								sender.sendMessage(Utils.clr(main.getLocale().getString("error-saving-jail")));
								e.printStackTrace();
							}
						}
						break;
					case "remove":
						File removed = new File(main.getDataFolder() + File.separator + "jails", args[1] + ".yml");
						if (removed.exists()) {
							if (!YamlConfiguration.loadConfiguration(removed).getBoolean("kept")) {
								removed.delete();
								sender.sendMessage(Utils.clr(main.getLocale().getString("removed")));
							} else {
								sender.sendMessage(Utils.clr(main.getLocale().getString("no-delete-kept")));
							}
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
			switch (args[0]) {
			case "reload":
				main.reloadConfig();
				main.reloadLocale();
				sender.sendMessage(Utils.clr(main.getLocale().getString("reloaded")));
				break;
			default:
				sender.sendMessage(Utils.clr(main.getLocale().getString("only-players")));
				break;
			}
		}
		return true;
	}

	private void throwInfo(CommandSender sender) {
		sender.sendMessage(Utils.clr("&eJails"));
		sender.sendMessage(Utils.clr("&eVersion: &a2.0"));
		sender.sendMessage(Utils.clr("&eAuthor: &aNukerFall"));
		sender.sendMessage(Utils.clr("&eType &a/jails help &efor help!"));
	}

	private void throwHelp(CommandSender sender, boolean permit) {
		sender.sendMessage(Utils.clr("&eJails &ahelp page"));
		sender.sendMessage(Utils.clr("&e/free &a- free yourself"));
		if (permit) {
			sender.sendMessage(Utils.clr("&e/jails help &a- opens this page"));
			sender.sendMessage(Utils.clr("&e/jails info &a- plugin info"));
			sender.sendMessage(Utils.clr("&e/jails list &a- list of jails"));
			sender.sendMessage(Utils.clr("&e/jails reload &a- reload configuration"));
			sender.sendMessage(Utils.clr("&e/jails playerlist &a- list of players"));
			sender.sendMessage(Utils.clr("&e/jails jail <name> &a- jail information"));
			sender.sendMessage(Utils.clr("&e/jails player <name> &a- player information"));
			sender.sendMessage(Utils.clr("&e/jails create <name> &a- create new jail"));
			sender.sendMessage(Utils.clr("&e/jails remove <name> &a- delete existing jail"));
			sender.sendMessage(Utils.clr("&e/jails setspawn &a- set spawnpoint for free player"));
			sender.sendMessage(Utils.clr("&e/jail <name> <time> <reason>/<null> &a- jail player"));
			sender.sendMessage(Utils.clr("&e/free <name> &a- set player free"));
		}
	}

}
