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

package com.yumahisai.blholebw.arena.feature;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.events.player.PlayerLeaveArenaEvent;
import com.yumahisai.blholebw.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;

public class SpoilPlayerTNTFeature {

    private static SpoilPlayerTNTFeature instance;
    private final LinkedList<Player> playersWithTnt = new LinkedList<>();

    private SpoilPlayerTNTFeature() {
        Bukkit.getPluginManager().registerEvents(new TNTListener(), BedWars.plugin);
        Bukkit.getScheduler().runTaskTimer(BedWars.plugin, new ParticleTask(), 20, 1L);
    }

    public static void init() {
        if (BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_PERFORMANCE_SPOIL_TNT_PLAYERS)) {
            if (instance == null) {
                instance = new SpoilPlayerTNTFeature();
            }
        }
    }

    private static class ParticleTask implements Runnable {

        @Override
        public void run() {
            for (Player player : instance.playersWithTnt) {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;
                BedWars.nms.playRedStoneDot(player);
            }
        }
    }

    private static class TNTListener implements Listener {

        @EventHandler
        public void onDie(PlayerKillEvent event) {
            instance.playersWithTnt.remove(event.getVictim());
        }

        @EventHandler
        public void onLeave(PlayerLeaveArenaEvent event) {
            instance.playersWithTnt.remove(event.getPlayer());
        }

        @EventHandler(ignoreCancelled = true)
        public void onPickUp(PlayerPickupItemEvent event) {
            if (event.getItem().getItemStack().getType() == Material.TNT) {
                IArena arena = Arena.getArenaByPlayer(event.getPlayer());
                if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) return;
                if (instance.playersWithTnt.contains(event.getPlayer())) return;
                instance.playersWithTnt.add(event.getPlayer());
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onDrop(PlayerDropItemEvent event) {
            if (event.getItemDrop().getItemStack().getType() == Material.TNT) {
                IArena arena = Arena.getArenaByPlayer(event.getPlayer());
                if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) return;
                if (!instance.playersWithTnt.contains(event.getPlayer())) return;
                if (event.getPlayer().getInventory().contains(Material.TNT)) return;
                instance.playersWithTnt.remove(event.getPlayer());
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlace(BlockPlaceEvent event) {
            ItemStack inHand = event.getItemInHand();
            IArena arena = Arena.getArenaByPlayer(event.getPlayer());
            if (arena == null || !arena.isPlayer(event.getPlayer()) || arena.isSpectator(event.getPlayer())) return;
            if (inHand.getType() == Material.TNT) {
                if (!instance.playersWithTnt.contains(event.getPlayer())) return;
                Bukkit.getScheduler().runTaskLater(BedWars.plugin,
                        () -> {
                            if (!event.getPlayer().getInventory().contains(Material.TNT)) {
                                instance.playersWithTnt.remove(event.getPlayer());
                            }
                        }, 1L);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void inventorySwitch(InventoryCloseEvent event) {
            Player player = (Player) event.getPlayer();
            IArena arena = Arena.getArenaByPlayer(player);
            if (arena == null || !arena.isPlayer(player) || arena.isSpectator(player)) return;
            if (instance.playersWithTnt.contains(player)) {
                if (player.getInventory().contains(Material.TNT)) return;
                instance.playersWithTnt.remove(player);
            } else if (!instance.playersWithTnt.contains(player)) {
                if (!player.getInventory().contains(Material.TNT)) return;
                instance.playersWithTnt.add(player);
            }
        }
    }
}
