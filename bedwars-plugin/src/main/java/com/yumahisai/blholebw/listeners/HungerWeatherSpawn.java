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

package com.yumahisai.blholebw.listeners;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class HungerWeatherSpawn implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (BedWars.getServerType() == ServerType.SHARED) {
            if (Arena.getArenaByPlayer((Player) e.getEntity()) != null) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            if (BedWars.getServerType() == ServerType.SHARED) {
                if (Arena.getArenaByIdentifier(e.getWorld().getName()) != null) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    //Used to prevent creature spawn
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (BedWars.getServerType() != ServerType.BUNGEE) {
                if (Arena.getArenaByIdentifier(e.getEntity().getWorld().getName()) != null) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) return;
        /* remove empty bottle */
        switch (e.getItem().getType()) {
            case GLASS_BOTTLE:
                BedWars.nms.minusAmount(e.getPlayer(), e.getItem(), 1);
                break;
            case MILK_BUCKET:
                e.setCancelled(true);
                BedWars.nms.minusAmount(e.getPlayer(), e.getItem(), 1);
                int task = Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
                    Arena.magicMilk.remove(e.getPlayer().getUniqueId());
                    BedWars.debug("PlayerItemConsumeEvent player " + e.getPlayer() + " was removed from magicMilk");
                }, 20 * 30L).getTaskId();
                Arena.magicMilk.put(e.getPlayer().getUniqueId(), task);
                break;
        }
    }

    @EventHandler
    //Prevent item spawning, issue #60
    public void onItemSpawn(ItemSpawnEvent e) {
        Location l = e.getEntity().getLocation();
        IArena a = Arena.getArenaByIdentifier(l.getWorld().getName());
        if (a == null) return;
        if (a.getStatus() != GameState.playing) {
            e.setCancelled(true);
        }
    }
}
