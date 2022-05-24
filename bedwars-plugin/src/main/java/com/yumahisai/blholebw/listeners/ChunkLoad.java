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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoad implements Listener {

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e){
        if (e == null) return;
        if (e.getChunk() == null) return;
        if (e.getChunk().getEntities() == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(BedWars.plugin, ()-> {
            for (Entity entity : e.getChunk().getEntities()){
                if (entity instanceof ArmorStand){
                    if (entity.hasMetadata("blholebw-setup")){
                        Bukkit.getScheduler().runTask(BedWars.plugin, entity::remove);
                        continue;
                    }
                    if (!((ArmorStand)entity).isVisible()){
                        if (((ArmorStand)entity).isMarker()){
                            //if (!entity.hasGravity()){
                            if (entity.isCustomNameVisible()){
                                if (ChatColor.stripColor(entity.getCustomName()).contains(" SET") || ChatColor.stripColor(entity.getCustomName()).contains(" set")){
                                    Bukkit.getScheduler().runTask(BedWars.plugin, entity::remove);
                                }
                            }
                            //}
                        }
                    }
                }
            }
        });
    }
}
