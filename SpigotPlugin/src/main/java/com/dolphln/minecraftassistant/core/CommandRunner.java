package com.dolphln.minecraftassistant.core;

import com.dolphln.minecraftassistant.Main;
import com.dolphln.minecraftassistant.models.VoiceCommand;
import com.dolphln.minecraftassistant.utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CommandRunner {

    private final Main plugin;

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
                // TODO: Create animation (should be made with a queue, to prevent multiple people to use it at the same time)
                player.getWorld().setTime(6000);

                plugin.sendMessage(player, "Making the weather sunny...");
            }
            case "make time rainy" -> {
                player.getWorld().setThundering(false);
                player.getWorld().setStorm(true);

                plugin.sendMessage(player, "Making the world rain...");
            }
            case "make time night" -> {
                player.getWorld().setTime(18000);

                plugin.sendMessage(player, "Making the weather night...");

            }
            case "make time storm" -> {
                player.getWorld().setThundering(true);
                player.getWorld().setThunderDuration(1000000);

                plugin.sendMessage(player, "Making the world storm...");
            }
            case "make time clear" -> {
                player.getWorld().setThundering(false);
                player.getWorld().setStorm(false);

                plugin.sendMessage(player, "Clearing the weather...");
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

                plugin.sendMessage(player, "Today is day " + (int) world.getGameTime() / 24000 + ". The weather is " + weather + " at &6" + world.getTime() + ".");
            }
            case "give me velocity" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 2, false));
                plugin.sendMessage(player, "Giving velocity...");
            }
            case "give me jump" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15 * 20, 2, false));
                plugin.sendMessage(player, "Giving jump...");
            }
            case "give me regeneration" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 3, true));
                plugin.sendMessage(player, "Giving regeneration...");
            }
            case "spawn a zombie" -> {
                spawnEntity(player, EntityType.ZOMBIE);
                plugin.sendMessage(player, "Spawning a zombie...");
            }
            case "spawn a creeper" -> {
                spawnEntity(player, EntityType.CREEPER);
                plugin.sendMessage(player, "Spawning a creeper...");
            }
            case "spawn a pig" -> {
                spawnEntity(player, EntityType.PIG);
                plugin.sendMessage(player, "Spawning a pig...");
            }
            case "spawn a cow" -> {
                spawnEntity(player, EntityType.COW);
                plugin.sendMessage(player, "Spawning a cow...");
            }
            case "spawn a horse" -> {
                spawnEntity(player, EntityType.HORSE);
                plugin.sendMessage(player, "Spawning a horse...");
            }
            case "give me diamonds" -> {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 13));
                plugin.sendMessage(player, "Giving diamonds...");
            }
            case "give me a sword" -> {
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD, 1));
                plugin.sendMessage(player, "Giving a sword...");
            }
        }
    }

    public void spawnEntity(Player player, EntityType entityType) {
        Location loc = player.getLocation();

        player.getWorld().spawnEntity(loc.add(RandomUtils.getRandomInt(-5, 5), 1, RandomUtils.getRandomInt(-5, 5)), entityType);
    }

    public boolean isRunning() {
        return this.bukkitTask != null;
    }
}
