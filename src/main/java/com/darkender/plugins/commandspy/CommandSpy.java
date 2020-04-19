package com.darkender.plugins.commandspy;

import com.darkender.plugins.commandspy.commands.ToggleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandSpy extends JavaPlugin implements Listener
{
    public static CommandSpy instance;
    
    private Map<UUID, CommandBar> playerBars;
    private Set<UUID> watching;
    
    @Override
    public void onEnable()
    {
        instance = this;
        playerBars = new HashMap<>();
        watching = new HashSet<>();
        for(Player p : getServer().getOnlinePlayers())
        {
            playerBars.put(p.getUniqueId(), new CommandBar(p.getName()));
        }
        getServer().getPluginManager().registerEvents(this, this);
    
        ToggleCommand toggleCommand = new ToggleCommand(this);
        getCommand("togglespy").setExecutor(toggleCommand);
        getCommand("togglespy").setTabCompleter(toggleCommand);
    }
    
    public void toggleWatching(Player player)
    {
        if(watching.contains(player.getUniqueId()))
        {
            for(CommandBar bar : playerBars.values())
            {
                bar.getBossBar().removePlayer(player);
            }
            watching.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "No longer watching commands");
        }
        else
        {
            for(CommandBar bar : playerBars.values())
            {
                bar.getBossBar().addPlayer(player);
            }
            watching.add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Now watching commands");
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        CommandBar bar = new CommandBar(event.getPlayer().getName());
        for(UUID watcher : watching)
        {
            bar.getBossBar().addPlayer(getServer().getPlayer(watcher));
        }
        playerBars.put(event.getPlayer().getUniqueId(), bar);
        
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        playerBars.remove(event.getPlayer().getUniqueId());
        watching.remove(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event)
    {
        playerBars.get(event.getPlayer().getUniqueId()).ranCommand(event.getMessage());
    }
    
    @EventHandler
    public void onTabComplete(TabCompleteEvent event)
    {
        if(event.getSender() instanceof Player)
        {
            Player p = (Player) event.getSender();
            playerBars.get(p.getUniqueId()).typingCommand(event.getBuffer());
        }
    }
}
