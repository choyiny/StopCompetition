package me.techmastary.plugins.stopcompetition;

/*
 * Thank you LaxWasHere, used his source for the base of the plugin.
 * https://github.com/LaxWasHere/Stop
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Stopcompetition extends JavaPlugin implements Listener {

	public String cmds = "cmds";
	public int stopTime;
	public boolean notify = true;
	public int basePlayers;
	public String prefix = "prefix";
	public boolean executedCommand;
	public boolean stopGameEnabled;

	public void onDisable() {
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		PluginDescriptionFile plugin = getDescription();
		System.out.println(plugin.getName() + " version " + plugin.getVersion() + " enabled.");
		getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		cmds = getConfig().getString("Commands");
		prefix = getConfig().getString("Prefix");
		executedCommand = false;
		stopGameEnabled = false;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(prefix + " This game doesn't work in console.");
			return false;
		}
		if (commandLabel.equalsIgnoreCase("startstopcompetition")) {
			if (sender.hasPermission("stop.admin")) {
				stopGameEnabled = true;
			}
		}
		if (commandLabel.equalsIgnoreCase("ssc")) {
			if (sender.hasPermission("stop.admin")) {
				stopGameEnabled = true;
				Bukkit.broadcastMessage("The game has started! Type /stop right now. The first person who types it will win the game!");
			}
		}
		if (commandLabel.equalsIgnoreCase("stop")) {
			if (stopGameEnabled) {
				if (executedCommand == false) {
					executedCommand = true;
					sender.sendMessage(ChatColor.GREEN + "" + prefix + ChatColor.GRAY + "You are the first person to type /stop!");
					Bukkit.broadcastMessage(ChatColor.GRAY + "" + sender.getName() + " won the game. Server is now shutting down ^^.");
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), getConfig().getString(cmds).replace("%player%", sender.getName()));
					this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							Bukkit.shutdown();
						}
					}, 100L);
					Bukkit.shutdown();
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "SORRY SOMEONE TYPED IT BEFORE YA :)");
					sender.sendMessage(ChatColor.RED + "YOU FAIL.");
					sender.sendMessage(ChatColor.RED + "YOU LOSE THE GAME.");
				}
			} else {
				return false;
			}
		}
		return true;
	}
}