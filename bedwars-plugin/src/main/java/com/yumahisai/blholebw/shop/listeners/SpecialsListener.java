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

package com.yumahisai.blholebw.shop.listeners;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.events.player.PlayerBedBugSpawnEvent;
import com.yumahisai.blholebw.api.events.player.PlayerDreamDefenderSpawnEvent;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpecialsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpecialInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        ItemStack i = e.getItem();
        if (i == null) return;
        if (i.getType() == Material.AIR) return;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return;
        if (a.getRespawnSessions().containsKey(e.getPlayer())) return;
        if (!a.isPlayer(p)) return;
        if (BedWars.shop.getYml().getBoolean(ConfigPath.SHOP_SPECIAL_SILVERFISH_ENABLE)) {
            if (!Misc.isProjectile(Material.valueOf(BedWars.shop.getYml().getString(ConfigPath.SHOP_SPECIAL_SILVERFISH_MATERIAL)))) {
                if (i.getType() == Material.valueOf(BedWars.shop.getYml().getString(ConfigPath.SHOP_SPECIAL_SILVERFISH_MATERIAL))
                        && BedWars.nms.itemStackDataCompare(i, (short) BedWars.shop.getYml().getInt(ConfigPath.SHOP_SPECIAL_SILVERFISH_DATA))) {
                    e.setCancelled(true);
                    ITeam playerTeam = a.getTeam(p);
                    PlayerBedBugSpawnEvent event = new PlayerBedBugSpawnEvent(p, playerTeam, a);
                    BedWars.nms.spawnSilverfish(p.getLocation().add(0, 1, 0), playerTeam, BedWars.shop.getYml().getDouble(ConfigPath.SHOP_SPECIAL_SILVERFISH_SPEED),
                            BedWars.shop.getYml().getDouble(ConfigPath.SHOP_SPECIAL_SILVERFISH_HEALTH), BedWars.shop.getInt(ConfigPath.SHOP_SPECIAL_SILVERFISH_DESPAWN),
                            BedWars.shop.getYml().getDouble(ConfigPath.SHOP_SPECIAL_SILVERFISH_DAMAGE));
                    Bukkit.getPluginManager().callEvent(event);
                    if (!BedWars.nms.isProjectile(i)) {
                        BedWars.nms.minusAmount(p, i, 1);
                        p.updateInventory();
                    }
                }
            }
        }
        if (BedWars.shop.getYml().getBoolean(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_ENABLE)) {
            if (!Misc.isProjectile(Material.valueOf(BedWars.shop.getYml().getString(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_MATERIAL)))) {
                if (i.getType() == Material.valueOf(BedWars.shop.getYml().getString(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_MATERIAL))
                        && BedWars.nms.itemStackDataCompare(i, (short) BedWars.shop.getYml().getInt(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_DATA))) {
                    e.setCancelled(true);
                    ITeam playerTeam = a.getTeam(p);
                    PlayerDreamDefenderSpawnEvent event = new PlayerDreamDefenderSpawnEvent(p, playerTeam, a);
                    BedWars.nms.spawnIronGolem(p.getLocation().add(0, 1, 0), playerTeam, BedWars.shop.getYml().getDouble(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_SPEED),
                            BedWars.shop.getYml().getDouble(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_HEALTH), BedWars.shop.getInt(ConfigPath.SHOP_SPECIAL_IRON_GOLEM_DESPAWN));
                    Bukkit.getPluginManager().callEvent(event);
                    if (!BedWars.nms.isProjectile(i)) {
                        BedWars.nms.minusAmount(p, i, 1);
                        p.updateInventory();
                    }
                }
            }
        }
    }
}
