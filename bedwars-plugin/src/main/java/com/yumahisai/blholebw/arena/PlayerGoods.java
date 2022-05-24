/*
 * BlackHoleBedWars
 * Copyright (c) 2022. YumaHisai
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.yumahisai.blholebw.arena;

import com.yumahisai.blholebw.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * This is where player stuff are stored so he can have them back after a game
 */
class PlayerGoods {

    private UUID uuid;
    private int level, foodLevel;
    private double health, healthscale;
    private float exp;
    private HashMap<ItemStack, Integer> items = new HashMap<>();
    private List<PotionEffect> potions = new ArrayList<>();
    private ItemStack[] armor;
    private HashMap<ItemStack, Integer> enderchest = new HashMap<>();
    private GameMode gamemode;
    private boolean allowFlight, flying;
    private String displayName, tabName;

    PlayerGoods(Player p, boolean prepare){
        this(p, prepare, false);
    }

    PlayerGoods(Player p, boolean prepare, boolean rejoin) {
        if (hasGoods(p)) {
            BedWars.plugin.getLogger().severe(p.getName() + " is already having a PlayerGoods vault :|");
            return;
        }
        this.uuid = p.getUniqueId();
        this.level = p.getLevel();
        this.exp = p.getExp();
        this.health = p.getHealth();
        this.healthscale = p.getHealthScale();
        this.foodLevel = p.getFoodLevel();
        playerGoods.put(p.getUniqueId(), this);
        int x = 0;
        for (ItemStack i : p.getInventory()) {
            if (i != null) {
                if (i.getType() != Material.AIR) {
                    items.put(i, x);
                }
            }
            x++;
        }
        for (PotionEffect ef : p.getActivePotionEffects()) {
            potions.add(ef);
            if (prepare) p.removePotionEffect(ef.getType());
        }
        armor = p.getInventory().getArmorContents();

        if (!rejoin) {
            int x2 = 0;
            for (ItemStack i : p.getEnderChest()) {
                if (i != null) {
                    if (i.getType() != Material.AIR) {
                        enderchest.put(i, x2);
                    }
                }
                x2++;
            }
        }

        this.gamemode = p.getGameMode();
        this.allowFlight = p.getAllowFlight();
        this.flying = p.isFlying();
        this.tabName = p.getPlayerListName();
        this.displayName = p.getDisplayName();

        /* prepare for arena */
        if (prepare) {
            p.setExp(0);
            p.setLevel(0);
            p.setHealthScale(20);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            if (!rejoin) {
                p.getEnderChest().clear();
            }
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }

    /**
     * a list where you can get PlayerGoods by player
     */
    private static HashMap<UUID, PlayerGoods> playerGoods = new HashMap<>();

    /**
     * check if a player has a vault
     */
    static boolean hasGoods(Player p) {
        return playerGoods.containsKey(p.getUniqueId());
    }

    /**
     * get a player vault
     */
    static PlayerGoods getPlayerGoods(Player p) {
        return playerGoods.get(p.getUniqueId());
    }

    /**
     * restore player
     */
    void restore() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        playerGoods.remove(player.getUniqueId());


        for (PotionEffect pf : player.getActivePotionEffects()) {
            player.removePotionEffect(pf.getType());
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setLevel(level);
        player.setExp(exp);
        player.setHealthScale(healthscale);
        try {
            player.setHealth(health);
        } catch (Exception e){
            BedWars.plugin.getLogger().severe("Something went wrong when restoring player health: "+health+". Giving default of: 20");
            player.setHealth(20);
        }
        player.setFoodLevel(foodLevel);

        if (!items.isEmpty()) {
            for (Map.Entry<ItemStack, Integer> entry : items.entrySet()) {
                player.getInventory().setItem(entry.getValue(), entry.getKey());
            }
            player.updateInventory();
            items.clear();
        }
        if (!potions.isEmpty()) {
            for (PotionEffect pe : potions) {
                player.addPotionEffect(pe);
            }
            potions.clear();
        }
        player.getEnderChest().clear();
        if (!enderchest.isEmpty()) {
            for (Map.Entry<ItemStack, Integer> entry : enderchest.entrySet()) {
                player.getEnderChest().setItem(entry.getValue(), entry.getKey());
            }
            enderchest.clear();
        }
        player.getInventory().setArmorContents(armor);
        player.setGameMode(gamemode);
        player.setAllowFlight(allowFlight);
        player.setFlying(flying);

        if (!displayName.equals(player.getDisplayName())) {
            player.setDisplayName(displayName);
        }
        if (!tabName.equals(player.getPlayerListName())) {
            player.setPlayerListName(tabName);
        }

        uuid = null;
        items = null;
        potions = null;
        armor = null;
        enderchest = null;
    }

}
