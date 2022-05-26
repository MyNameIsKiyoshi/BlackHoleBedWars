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

package com.yumahisai.blholebw.support.vipfeatures;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.events.player.PlayerJoinArenaEvent;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.api.vipfeatures.IVipFeatures;
import com.yumahisai.blholebw.api.vipfeatures.event.BlockChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

public class VipListeners implements Listener {

    private final IVipFeatures api;

    public VipListeners(IVipFeatures api) {
        this.api = api;
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent e) {
        if (BedWars.getServerType() == ServerType.MULTIARENA) {
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> api.givePlayerItemStack(e.getPlayer()), 10L);
        }
    }

    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent e) {
        Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> api.givePlayerItemStack(e.getPlayer()), 10L);
    }

    @EventHandler
    public void onBockChange(BlockChangeEvent e) {
        if (BedWars.getAPI().getArenaUtil().getArenaByName(e.getLocation().getWorld().getName()) != null) {
            IArena a = BedWars.getAPI().getArenaUtil().getArenaByName(e.getLocation().getWorld().getName());
            for (ITeam t : a.getTeams()) {
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        if (e.getLocation().getBlockX() == t.getBed().getBlockX() &&
                                e.getLocation().getBlockY() == t.getBed().getBlockY() &&
                                e.getLocation().getBlockZ() == t.getBed().getBlockZ()) {
                            if (BedWars.nms.isBed(t.getBed().clone().add(x, 0, z).getBlock().getType())) e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            a.getPlaced().add(new Vector(e.getLocation().getBlockX(), e.getLocation().getBlockY(),e.getLocation().getBlockZ()));
        }
    }
}
