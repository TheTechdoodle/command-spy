package com.darkender.plugins.commandspy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CommandBar
{
    public static final int COMMAND_FADE_TICKS = 80;
    public static final int TYPING_FADE_DELAY = 30;
    public static final int TYPING_FADE_TICKS = 30;
    
    private BossBar bossBar;
    private String name;
    private BukkitTask fadeTask = null;
    private BukkitTask typingFadeDelay = null;
    
    public CommandBar(String name)
    {
        this.name = name;
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        this.bossBar.setVisible(false);
    }
    
    public void close()
    {
        cancelFadeDelayTask();
        cancelFadeTask();
        this.bossBar.removeAll();
    }
    
    public BossBar getBossBar()
    {
        return bossBar;
    }
    
    public void typingCommand(String command)
    {
        cancelFadeTask();
        
        bossBar.setColor(BarColor.YELLOW);
        bossBar.setProgress(1.0);
        bossBar.setTitle(ChatColor.GOLD + name + ": " + ChatColor.BLUE + command);
        bossBar.setVisible(true);
    
        if(typingFadeDelay != null)
        {
            typingFadeDelay.cancel();
        }
        typingFadeDelay = (new BukkitRunnable()
        {
            @Override
            public void run()
            {
                fade(TYPING_FADE_TICKS);
                typingFadeDelay = null;
            }
        }).runTaskLater(CommandSpy.instance, TYPING_FADE_DELAY);
    }
    
    public void ranCommand(String command)
    {
        cancelFadeDelayTask();
        
        bossBar.setColor(BarColor.GREEN);
        bossBar.setProgress(1.0);
        bossBar.setTitle(ChatColor.GOLD + name + ": " + ChatColor.BLUE + command);
        bossBar.setVisible(true);
        
        fade(COMMAND_FADE_TICKS);
    }
    
    private void cancelFadeTask()
    {
        if(fadeTask != null)
        {
            fadeTask.cancel();
            fadeTask = null;
        }
    }
    
    private void cancelFadeDelayTask()
    {
        if(typingFadeDelay != null)
        {
            typingFadeDelay.cancel();
            typingFadeDelay = null;
        }
    }
    
    private void fade(int ticks)
    {
        if(fadeTask != null)
        {
            fadeTask.cancel();
        }
        fadeTask = (new BukkitRunnable()
        {
            @Override
            public void run()
            {
                double progress = bossBar.getProgress();
                progress -= (1.0 / ticks);
                if(progress < 0)
                {
                    bossBar.setVisible(false);
                    fadeTask.cancel();
                    fadeTask = null;
                }
                else
                {
                    bossBar.setProgress(progress);
                }
            }
        }).runTaskTimer(CommandSpy.instance, 0L, 1L);
    }
}
