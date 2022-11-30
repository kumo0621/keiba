package com.github.kumo0621.keiba;

import jdk.internal.org.jline.reader.ParsedLine;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Keiba extends JavaPlugin implements org.bukkit.event.Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    FileConfiguration config;

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    int number = 0;

    @EventHandler
    public void clickJoin(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getScoreboardTags().contains("join") && 0 <= number && number <= 10) {
            config.set(String.valueOf(number), player);
            number++;
            saveConfig();
            player.sendMessage(player + "さんの参加を認めます。");
        } else {
            player.sendMessage("満員です。");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("kakeru")) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                PlayerInventory inventory = Objects.requireNonNull(player).getInventory();
                if (args.length <= 1) {
                    sender.sendMessage("/kakeru 名前 かけるダイヤの個数");
                } else if (inventory.containsAtLeast(new ItemStack(Material.DIAMOND), Integer.parseInt(args[1]))) {
                    config.set(player.getName(), args[0]);
                    config.set(player.getName()+"setting", args[1]);
                    saveConfig();
                    sender.sendMessage(args[0] + "さんに" + args[1] + "個かけました。");
                } else {
                    sender.sendMessage("掛け金が足りません");
                    //sender.sendMessage("かけた人は"+(String) Objects.requireNonNull(config.get(player.getName())));
                    //sender.sendMessage("かけた額は"+config.get(player.getName()+"setting"));
                }

            }
        }

        if (command.getName().equals("start")) {
            if (sender instanceof Player) {

            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
