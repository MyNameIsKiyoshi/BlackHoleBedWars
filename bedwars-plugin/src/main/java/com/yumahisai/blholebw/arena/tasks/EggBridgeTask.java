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

package com.yumahisai.blholebw.arena.tasks;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.TeamColor;
import com.yumahisai.blholebw.api.events.gameplay.EggBridgeBuildEvent;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.configuration.Sounds;
import com.yumahisai.blholebw.listeners.EggBridge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("WeakerAccess")
public class EggBridgeTask implements Runnable {

    private Egg projectile;
    private TeamColor teamColor;
    private Player player;
    private IArena arena;
    private BukkitTask task;

    public EggBridgeTask(Player player, Egg projectile, TeamColor teamColor) {
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) return;
        this.arena = a;
        this.projectile = projectile;
        this.teamColor = teamColor;
        this.player = player;
        task = Bukkit.getScheduler().runTaskTimer(BedWars.plugin, this, 0, 1);
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public Egg getProjectile() {
        return projectile;
    }

    public Player getPlayer() {
        return player;
    }

    public IArena getArena() {
        return arena;
    }

    @Override
    public void run() {

        Location loc = getProjectile().getLocation();

        if (getProjectile().isDead()
                || !arena.isPlayer(getPlayer())
                || getPlayer().getLocation().distance(getProjectile().getLocation()) > 27
                || getPlayer().getLocation().getY() - getProjectile().getLocation().getY() > 9) {
            EggBridge.removeEgg(projectile);
            return;
        }

        if (getPlayer().getLocation().distance(loc) > 4.0D) {

            Block b2 = loc.clone().subtract(0.0D, 2.0D, 0.0D).getBlock();
            if (!Misc.isBuildProtected(b2.getLocation(), getArena())) {
                if (b2.getType() == Material.AIR) {
                    b2.setType(BedWars.nms.woolMaterial());
                    BedWars.nms.setBlockTeamColor(b2, getTeamColor());
                    getArena().addPlacedBlock(b2);
                    Bukkit.getPluginManager().callEvent(new EggBridgeBuildEvent(getTeamColor(), getArena(), b2));
                    loc.getWorld().playEffect(b2.getLocation(), BedWars.nms.eggBridge(), 3);
                    Sounds.playSound("egg-bridge-block", getPlayer());
                }
            }

            Block b3 = loc.clone().subtract(1.0D, 2.0D, 0.0D).getBlock();
            if (!Misc.isBuildProtected(b3.getLocation(), getArena())) {
                if (b3.getType() == Material.AIR) {
                    b3.setType(BedWars.nms.woolMaterial());
                    BedWars.nms.setBlockTeamColor(b3, getTeamColor());
                    getArena().addPlacedBlock(b3);
                    Bukkit.getPluginManager().callEvent(new EggBridgeBuildEvent(getTeamColor(), getArena(), b3));
                    loc.getWorld().playEffect(b3.getLocation(), BedWars.nms.eggBridge(), 3);
                    Sounds.playSound("egg-bridge-block", getPlayer());
                }
            }

            Block b4 = loc.clone().subtract(0.0D, 2.0D, 1.0D).getBlock();
            if (!Misc.isBuildProtected(b4.getLocation(), getArena())) {
                if (b4.getType() == Material.AIR) {
                    b4.setType(BedWars.nms.woolMaterial());
                    BedWars.nms.setBlockTeamColor(b4, getTeamColor());
                    getArena().addPlacedBlock(b4);
                    Bukkit.getPluginManager().callEvent(new EggBridgeBuildEvent(getTeamColor(), getArena(), b4));
                    loc.getWorld().playEffect(b4.getLocation(), BedWars.nms.eggBridge(), 3);
                    Sounds.playSound("egg-bridge-block", getPlayer());
                }
            }
        }
    }

    public void cancel(){
        task.cancel();
    }
}
