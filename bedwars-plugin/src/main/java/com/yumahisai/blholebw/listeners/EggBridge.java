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
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.events.gameplay.EggBridgeThrowEvent;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.tasks.EggBridgeTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EggBridge implements Listener {

    //Active eggBridges
    private static HashMap<Egg, EggBridgeTask> bridges = new HashMap<>();

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (BedWars.getServerType() == ServerType.MULTIARENA) {
            if (event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getEntity() instanceof Egg) {
            Egg projectile = (Egg) event.getEntity();
            if (projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();
                IArena arena = Arena.getArenaByPlayer(shooter);
                if (arena != null) {
                    if (arena.isPlayer(shooter)) {
                        EggBridgeThrowEvent throwEvent = new EggBridgeThrowEvent(shooter, arena);
                        Bukkit.getPluginManager().callEvent(throwEvent);
                        if (event.isCancelled()) {
                            event.setCancelled(true);
                            return;
                        }
                        bridges.put(projectile, new EggBridgeTask(shooter, projectile, arena.getTeam(shooter).getColor()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg) {
            removeEgg((Egg) e.getEntity());
        }
    }

    /**
     * Remove an egg from the active eggs list
     *
     * @since API 7
     */
    public static void removeEgg(Egg e) {
        if (bridges.containsKey(e)) {
            if (bridges.get(e) != null) {
                bridges.get(e).cancel();
            }
            bridges.remove(e);
        }
    }

    /**
     * Get active egg bridges.
     * Modified  in api 11
     *
     * @since API 11
     */
    public static Map<Egg, EggBridgeTask> getBridges() {
        return Collections.unmodifiableMap(bridges);
    }
}
