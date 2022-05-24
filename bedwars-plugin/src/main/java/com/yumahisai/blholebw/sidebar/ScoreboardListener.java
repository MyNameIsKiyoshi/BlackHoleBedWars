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

package com.yumahisai.blholebw.sidebar;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.events.player.PlayerBedBreakEvent;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.events.player.PlayerReJoinEvent;
import com.yumahisai.blholebw.api.events.player.PlayerReSpawnEvent;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class ScoreboardListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        if (!(e.getEntity() instanceof Player)) return;
        final Player player = (Player) e.getEntity();
        final IArena arena = Arena.getArenaByPlayer(player);

        int health = (int) Math.ceil((player.getHealth() - e.getFinalDamage()));

        if (arena == null) return;

        for (BedWarsScoreboard scoreboard : BedWarsScoreboard.getScoreboards().values()) {
            if (arena.equals(scoreboard.getArena())) {
                scoreboard.getHandle().refreshHealth(player, health);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegain(EntityRegainHealthEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        if (!(e.getEntity() instanceof Player)) return;
        final Player player = (Player) e.getEntity();
        final IArena arena = Arena.getArenaByPlayer(player);
        if (arena == null) return;

        int health = (int) Math.ceil(player.getHealth() + e.getAmount());

        for (BedWarsScoreboard scoreboard : BedWarsScoreboard.getScoreboards().values()) {
            if (arena.equals(scoreboard.getArena())) {
                scoreboard.getHandle().refreshHealth(player, health);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onReSpawn(PlayerReSpawnEvent e) {
        if (e == null) return;
        final IArena arena = e.getArena();
        for (BedWarsScoreboard scoreboard : BedWarsScoreboard.getScoreboards().values()) {
            if (arena.equals(scoreboard.getArena())) {
                scoreboard.getHandle().refreshHealth(e.getPlayer(), (int) Math.ceil(e.getPlayer().getHealth()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void reJoin(PlayerReJoinEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        if (!BedWars.config.getBoolean(ConfigPath.SB_CONFIG_SIDEBAR_LIST_FORMAT_PLAYING)) return;
        final IArena arena = e.getArena();
        final Player player = e.getPlayer();

        // re-add player to scoreboard tab list
        for (BedWarsScoreboard scoreboard : BedWarsScoreboard.getScoreboards().values()) {
            if (arena.equals(scoreboard.getArena())) {
                scoreboard.addToTabList(player, Messages.FORMATTING_SCOREBOARD_TAB_PREFIX_PLAYING, Messages.FORMATTING_SCOREBOARD_TAB_SUFFIX_PLAYING);
            }
        }
    }

    @EventHandler
    public void onBedDestroy(PlayerBedBreakEvent e) {
        if (e == null) return;
        final IArena arena = e.getArena();

        // refresh placeholders in case placeholders refresh is disabled
        BedWarsScoreboard.getScoreboards().values().forEach(bedWarsScoreboard -> {
            if (arena.equals(bedWarsScoreboard.getArena())) {
                bedWarsScoreboard.getHandle().refreshPlaceholders();
            }
        });
    }

    @EventHandler
    public void onFinalKill(PlayerKillEvent e) {
        if (e == null) return;
        if (!e.getCause().isFinalKill()) return;
        final IArena arena = e.getArena();

        // refresh placeholders in case placeholders refresh is disabled
        BedWarsScoreboard.getScoreboards().values().forEach(bedWarsScoreboard -> {
            if (arena.equals(bedWarsScoreboard.getArena())) {
                bedWarsScoreboard.getHandle().refreshPlaceholders();
            }
        });
    }
}
