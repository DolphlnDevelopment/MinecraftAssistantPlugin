package com.dolphln.minecraftassistant.core;

import com.dolphln.minecraftassistant.Main;
import com.dolphln.minecraftassistant.models.VoiceCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class CommandRunner {

    private final Main plugin;
    private final String prefix = "&6[&eAssistant&6] ";

    private BukkitTask bukkitTask;

    public CommandRunner(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (this.bukkitTask != null) return;

        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                VoiceCommand voiceCommand = plugin.getSocketServer().getNextCommand();
                if (voiceCommand == null) return;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player player = Bukkit.getPlayer(voiceCommand.getPlayerUUID());
                        if (player == null) {
                            voiceCommand.getClient().close();
                            return;
                        }

                        runCommand(player, voiceCommand.getCommand().toLowerCase());
                    }
                }.runTask(plugin);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    public void stop() {
        if (this.bukkitTask == null) return;

        this.bukkitTask.cancel();
        this.bukkitTask = null;
    }

    public void runCommand(Player player, String command) {
        switch (command) {
            case "make time sunny" -> {
                player.getWorld().setTime(6000);
            }
            case "make time rainy" -> {
                player.getWorld().setThundering(false);
                player.getWorld().setStorm(true);
            }
            case "make time night" -> {
                player.getWorld().setTime(18000);
            }
            case "make time storm" -> {
                player.getWorld().setThundering(true);
                player.getWorld().setThunderDuration(1000000);
            }
            case "make time clear" -> {
                player.getWorld().setThundering(false);
                player.getWorld().setStorm(false);
            }
            case "what's the weather" -> {
                World world = player.getWorld();
                String weather = "";
                if (world.isThundering()) {
                    weather = "thundering";
                } else if (world.hasStorm()) {
                    weather = "rainy";
                } else {
                    weather = "clear";
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&fToday is day " + (int) world.getGameTime()/24000 + ". The weather is " + weather + " at &6" + world.getTime() + "."));
            }
            case "give me velocity" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 2, false));
            }
            case "give me jump" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15*20, 2, false));
            }
            case "give me regeneration" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5*20, 3, true));
            }
            case "spawn a zombie" -> {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            }
            case "spawn a creeper" -> {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.CREEPER);

            }
            case "spawn a pig" -> {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);

            }
            case "spawn a cow" -> {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.COW);

            }
            case "spawn a horse" -> {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);

            }
            case "give me diamonds" -> {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 13));
            }
            case "give me a sword" -> {
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD, 1));
            }
        }
    }

    public boolean isRunning() {
        return this.bukkitTask != null;
    }
}
