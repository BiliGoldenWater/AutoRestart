package io.github.biligoldenwater.autorestart.commands;

import io.github.biligoldenwater.autorestart.modules.CheckPermissions;
import io.github.biligoldenwater.autorestart.modules.RestartTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAutoRestart {
    JavaPlugin plugin;
    Configuration config;
    final byte Help_ALL = 0;
    final byte Help_Restart = 1;

    public CommandAutoRestart(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.plugin.getCommand("autorestart").setExecutor(autorestart_command);
    }

    CommandExecutor autorestart_command = new CommandExecutor() {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            switch (args.length) {
                case 0:
                    if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.help")) return true;
                    sendHelpMessage(sender, Help_ALL);
                    return true;
                case 1:
                    switch (args[0]) {
                        case "help":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.help")) return true;
                            sendHelpMessage(sender, Help_ALL);
                            return true;
                        case "reload":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.reload"))
                                return true;

                            if (RestartTimer.isTimerRunning()) {
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Restart timer is running,stopping it.");
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "If you need restart it,please use /autorestart start or /autorestart restart <second> for restart.");
                                RestartTimer.stopRestartTimer();
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Stopped,reload config.");
                            }

                            plugin.reloadConfig();
                            config = plugin.getConfig();
                            plugin.getLogger().info("Reloaded");
                            sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Config reloaded.");
                            return true;
                        case "restart":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.restart"))
                                return true;
                            sendHelpMessage(sender, Help_Restart);
                            return true;
                        case "cancel":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.cancel"))
                                return true;
                            if (RestartTimer.isTimerRunning()) {
                                RestartTimer.stopRestartTimer();
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Canceled.");
                            } else {
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Can't cancel it,because it isn't running.");
                            }
                            return true;
                        case "start":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.start"))
                                return true;
                            if (!RestartTimer.isTimerRunning()) {
                                if (config.getInt("restartTime") <= 600) {
                                    config.set("restartTime", 600);
                                }
                                RestartTimer.startRestartTimer(plugin, config.getInt("restartTime"));
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Started.");
                            } else {
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Can't start it,because it's running.");
                            }

                            return true;
                        case "show":
                            if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.show")) return true;

                            if (RestartTimer.isTimerRunning()) {
                                long timeLeft = RestartTimer.getTimeLeft();

                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + (int) timeLeft / 60 / 60 + " hour(s) " + (int) timeLeft / 60 % 60 + " minute(s) " + (int) timeLeft % 60 + " second(s)" + " left.");
                            } else {
                                sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Can't get how many time left,because timer is not running.");
                            }

                            return true;
                    }
                case 2:
                    if (args[0].equals("restart")) {
                        if (!CheckPermissions.hasPermissions_Tips(sender, "autorestart.commands.restart")) return true;
                        int time = 60;
                        try {
                            time = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            sendHelpMessage(sender, Help_Restart);
                        }

                        if (RestartTimer.isTimerRunning()) {
                            sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Timer is running,stopping it.");
                            RestartTimer.stopRestartTimer();
                            sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Stopped,start restart timer.");
                        }
                        RestartTimer.startRestartTimer(plugin, time);
                        sender.sendMessage(config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + "Started restart timer.");

                        return true;
                    }
            }
            return false;
        }
    };

    private void sendHelpMessage(CommandSender sender, byte level) {
        switch (level) {
            case Help_ALL:
                sender.sendMessage("AutoRestart By.Golden_Water");
                sender.sendMessage("Usage: /autorestart help (Show this message.)");
                sender.sendMessage("Usage: /autorestart restart <time> (Restart after given time.(seconds))");
                sender.sendMessage("Usage: /autorestart cancel (Cancel restart,if it's running.)");
                sender.sendMessage("Usage: /autorestart start (Start restart timer,if it's canceled.)");
                sender.sendMessage("Usage: /autorestart show (Show how many time left.)");
                sender.sendMessage("Usage: /autorestart reload (Reload config.)");
                break;
            case Help_Restart:
                sender.sendMessage("Usage: /autorestart restart <time> (Restart after given time.(seconds))");
                break;
        }
    }
}
