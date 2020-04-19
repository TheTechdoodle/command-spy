package com.darkender.plugins.commandspy.commands;

import com.darkender.plugins.commandspy.CommandSpy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ToggleCommand implements CommandExecutor, TabCompleter
{
    private static final List<String> empty = new ArrayList<>();
    private final CommandSpy commandSpy;
    
    public ToggleCommand(CommandSpy commandSpy)
    {
        this.commandSpy = commandSpy;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
            return true;
        }
        
        Player p  = (Player) sender;
        commandSpy.toggleWatching(p);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return empty;
    }
}
