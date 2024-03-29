package com.github.kumo0621.keiba;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
    Map<Integer, String> joinMap = new HashMap<>();

    @EventHandler
    public void clickJoin(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!start) {
            if (event.getRightClicked().getScoreboardTags().contains("join") && 0 <= number && number <= 10) {
                joinMap.put(syuukai, player.getName());
                number++;

                player.sendMessage(player.getName() + "さんの参加を認めます。");
            } else if (event.getRightClicked().getScoreboardTags().contains("join") && 11 <= number && number <= 100) {
                player.sendMessage("満員です。");
            }
        }
    }

    int syuukai = 0;
    boolean start = false;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        //プレイヤーは地面に立っているか
        //移動先の座標[を得る
        Location loc = e.getTo().clone();

        //座標を１０センチ下に移動する
        loc.add(0, -1, 0);
        Player player = e.getPlayer();
        //その座標にはレッドストーンブロックがあるか
        if (start) {
            if (loc.getBlock().getType().equals(Material.REDSTONE_BLOCK)) {
                if (joinMap.containsValue(player)) {
                    if (joinMap.containsKey(0)) {
                        syuukai++;
                        player.sendMessage("1周目");
                    }
                }
            } else if (loc.getBlock().getType().equals(Material.IRON_BLOCK) && syuukai == 1) {
                config.set(player.getName() + "syuukai", syuukai);
                syuukai++;
                player.sendMessage("2周目");
            } else if (loc.getBlock().getType().equals(Material.GOLD_BLOCK) && syuukai == 2) {
                config.set(player.getName() + "syuukai", syuukai);
                syuukai++;
                player.sendMessage("3周目");
            } else if (loc.getBlock().getType().equals(Material.BEDROCK) && syuukai == 3) {
                config.set(player.getName() + "syuukai", syuukai);
                syuukai++;
                player.sendMessage("ゴール");
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("kakeru")) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                PlayerInventory inventory = Objects.requireNonNull(player).getInventory();
                if (args.length <= 1) {
                    sender.sendMessage("/kakeru かける名前 かけるダイヤの個数");
                } else if (inventory.containsAtLeast(new ItemStack(Material.DIAMOND), Integer.parseInt(args[1]))) {
                    config.set(player.getName(), args[0]);
                    config.set(player.getName() + "setting", args[1]);
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
                start = true;
                System.out.println(joinMap);
            }
        }
        return super.onCommand(sender, command, label, args);

    }
}
