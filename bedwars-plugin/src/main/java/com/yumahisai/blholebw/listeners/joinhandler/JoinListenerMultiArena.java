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

package com.yumahisai.blholebw.listeners.joinhandler;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.ReJoin;
import com.yumahisai.blholebw.sidebar.BedWarsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinListenerMultiArena implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        final Player p = e.getPlayer();
        p.getInventory().setArmorContents(null);

        JoinHandlerCommon.displayCustomerDetails(p);

        // Show commands if player is op and there is no set arenas
        if (p.isOp()) {
            if (Arena.getArenas().isEmpty()) {
                p.performCommand(BedWars.mainCmd);
            }
        }

        ReJoin reJoin = ReJoin.getPlayer(p);

        Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
            // Hide new player to players and spectators, and vice versa
            // Players from lobby will remain visible
            for (Player online : Bukkit.getOnlinePlayers()){
                if (Arena.isInArena(online)) {
                    BedWars.nms.spigotHidePlayer(online, p);
                    BedWars.nms.spigotHidePlayer(p, online);
                } else {
                    BedWars.nms.spigotShowPlayer(online, p);
                    BedWars.nms.spigotShowPlayer(p, online);
                }
            }

            // To prevent invisibility issues handle ReJoin after sending invisibility packets
            if (reJoin != null) {
                if (reJoin.canReJoin()) {
                    reJoin.reJoin(p);
                    return;
                }
                reJoin.destroy(false);
            }
        }, 14L);

        if (reJoin != null && reJoin.canReJoin()) return;

        // Teleport to lobby location
        Location lobbyLocation = BedWars.config.getConfigLoc("lobbyLoc");
        if (lobbyLocation != null && lobbyLocation.getWorld() != null) {
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> p.teleport(lobbyLocation, PlayerTeleportEvent.TeleportCause.PLUGIN), 2L);
        }

        // Send items
        Arena.sendLobbyCommandItems(p);

        BedWarsScoreboard.giveScoreboard(p, null, true);

        p.setHealthScale(p.getMaxHealth());
        p.setExp(0);
        p.setHealthScale(20);
        p.setFoodLevel(20);
    }
}

