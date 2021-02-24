package io.github.biligoldenwater.autorestart.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RestartTimer {
    private static boolean restartTimerRunning = false;
    private static long timeLeft = -1;

    public static void startRestartTimer(JavaPlugin plugin,int restartAtTime){
        Configuration config = plugin.getConfig();

        BukkitRunnable restartTimer = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                restartTimerRunning = true;
                long currentTime = System.currentTimeMillis() / 1000;
                long startTime = currentTime;
                long restartTime = startTime + restartAtTime;
                long lastTime = currentTime;
                timeLeft = restartTime - currentTime;
                List<Integer> tipTimes = config.getIntegerList("restartReminderTime");
                int i=0;
                while (i<tipTimes.size()){
                    if (tipTimes.get(i) > restartTime - currentTime) {
                        tipTimes.remove(i);
                    } else {
                        ++i;
                    }
                }

                if(restartAtTime>600) {
                    tipTimeLeft(config,plugin);
                }

                while (currentTime <= restartTime) {
                    if (!restartTimerRunning){
                        plugin.getLogger().info("Stop Timer.");
                        break;
                    }
                    currentTime = System.currentTimeMillis() / 1000;
                    if (lastTime == currentTime) continue;
                    timeLeft = restartTime - currentTime;

                    i=0;
                    while (i<tipTimes.size()){
                        if (tipTimes.get(i) >= restartTime - currentTime) {
                            tipTimeLeft(config,plugin);
                            tipTimes.remove(i);
                        } else {
                            ++i;
                        }
                    }

                    lastTime = currentTime;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (restartTimerRunning) {
                    Bukkit.getScheduler().runTask(Bukkit.getServer().getPluginManager().getPlugin("AutoRestart"), new Runnable() {
                        @Override
                        public void run() {
                            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                player.kickPlayer(config.getString("restartKickTip", "§a服务器正在§c§l重启§a,请稍后尝试加入服务器"));
                            }

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"save-all");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Bukkit.getServer().shutdown();
                        }
                    });
                }
            }
        };
        restartTimer.runTaskAsynchronously(plugin);
    }

    private static void tipTimeLeft(Configuration config,JavaPlugin plugin){
        String restartTip = config.getString("tipsPrefix", "§b[§9AutoRestart§b]") + " " + config.getString("restartTip", "§a服务器将在§d§l %Hours% §a小时§d§l %Minutes% §a分§d§l %Seconds% §a秒后§c§l重启");
        restartTip = restartTip.replaceAll("%Hours%", String.valueOf((int) timeLeft / 60 / 60)).replaceAll("%Minutes%", String.valueOf((int) timeLeft / 60 % 60)).replaceAll("%Seconds%", String.valueOf((int) timeLeft % 60));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(restartTip);
        }
        plugin.getLogger().info(restartTip);
    }

    public static void stopRestartTimer(){
        restartTimerRunning = false;
    }

    public static boolean isTimerRunning(){
        return restartTimerRunning;
    }

    public static long getTimeLeft(){
        return timeLeft;
    }
}
