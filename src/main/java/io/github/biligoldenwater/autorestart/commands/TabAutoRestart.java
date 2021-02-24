package io.github.biligoldenwater.autorestart.commands;

import io.github.biligoldenwater.autorestart.modules.CheckPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TabAutoRestart {

    public TabAutoRestart(JavaPlugin plugin){
        plugin.getCommand("autorestart").setTabCompleter(autorestart_tab);
    }

    TabCompleter autorestart_tab = new TabCompleter() {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            switch (args.length){
                case 1:
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.help") && "help".startsWith(args[0]))completions.add("help");
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.reload") && "reload".startsWith(args[0]))completions.add("reload");
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "restart".startsWith(args[0]))completions.add("restart");
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.cancel") && "cancel".startsWith(args[0]))completions.add("cancel");
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.start") && "start".startsWith(args[0]))completions.add("start");
                    if(CheckPermissions.hasPermissions(sender,"autorestart.commands.show") && "show".startsWith(args[0]))completions.add("show");
                    return completions;
                case 2:
                    if(args[0].equals("restart")){
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "600".startsWith(args[1]))completions.add("600");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "300".startsWith(args[1]))completions.add("300");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "120".startsWith(args[1]))completions.add("120");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "60".startsWith(args[1]))completions.add("60");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "30".startsWith(args[1]))completions.add("30");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "20".startsWith(args[1]))completions.add("20");
                        if(CheckPermissions.hasPermissions(sender,"autorestart.commands.restart") && "10".startsWith(args[1]))completions.add("10");
                        return completions;
                    }
            }
            return new ArrayList<>();
        }
    };
}
