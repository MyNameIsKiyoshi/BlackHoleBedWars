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
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.LastHit;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.arena.team.BedWarsTeam;
import com.yumahisai.blholebw.commands.bedwars.subcmds.regular.CmdStats;
import com.yumahisai.blholebw.sidebar.BedWarsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class QuitAndTeleportListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        // Remove from arena
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            if (a.isPlayer(p)) {
                a.removePlayer(p, true);
            } else if (a.isSpectator(p)) {
                a.removeSpectator(p, true);
            }
        }

        //Save preferred language
        if (Language.getLangByPlayer().containsKey(p.getUniqueId())) {
            final UUID u = p.getUniqueId();
            Bukkit.getScheduler().runTaskAsynchronously(BedWars.plugin, () -> {
                String iso = Language.getLangByPlayer().get(p.getUniqueId()).getIso();
                if (Language.isLanguageExist(iso)) {
                    if (BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_DISABLED_LANGUAGES).contains(iso))
                        iso = Language.getDefaultLanguage().getIso();
                    BedWars.getRemoteDatabase().setLanguage(u, iso);
                }
                Language.getLangByPlayer().remove(p.getUniqueId());
            });
        }

        if (BedWars.getServerType() != ServerType.SHARED) {
            e.setQuitMessage(null);
        }
        // Manage internal parties
        if (BedWars.getParty().isInternal()) {
            if (BedWars.getParty().hasParty(p)) {
                BedWars.getParty().removeFromParty(p);
            }
        }
        // Check if was doing a setup and remove the session
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss != null) {
            ss.cancel();
        }

        BedWarsScoreboard sb = BedWarsScoreboard.getSBoard(e.getPlayer().getUniqueId());
        if (sb != null) {
            sb.remove();
        }

        BedWarsTeam.reSpawnInvulnerability.remove(e.getPlayer().getUniqueId());

        LastHit lh = LastHit.getLastHit(p);
        if (lh != null) {
            lh.remove();
        }

        CmdStats.getStatsCoolDown().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        if (e.getTo() == null) return;
        if (e.getTo().getWorld() == null) return;
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a != null) {
            IArena a1 = Arena.getArenaByIdentifier(e.getTo().getWorld().getName());
            if (a1 != null) {
                if (!a1.equals(a)) {
                    if (a.isSpectator(e.getPlayer())) a.removeSpectator(e.getPlayer(), false);
                    if (a.isPlayer(e.getPlayer())) a.removePlayer(e.getPlayer(), false);
                    e.getPlayer().sendMessage("PlayerTeleportEvent something went wrong. You have joined an arena world while playing on a different map.");
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        // I think this for shared mode should be removed
        if (BedWars.getServerType() == ServerType.SHARED) {
            if (BedWars.config.getBoolean(ConfigPath.SB_CONFIG_SIDEBAR_USE_LOBBY_SIDEBAR)) {
                //Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                if (e.getPlayer().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
                    BedWarsScoreboard.giveScoreboard(e.getPlayer(), null, true);
                } else {
                    BedWarsScoreboard sb = BedWarsScoreboard.getSBoard(e.getPlayer().getUniqueId());
                    if (sb != null) {
                        sb.remove();
                    }
                }
                //}, 2L);
            }
        }
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a != null) {
            if (a.isPlayer(e.getPlayer())) {
                if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting) return;
                if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(a.getWorld().getName())) {
                    a.removePlayer(e.getPlayer(), BedWars.getServerType() == ServerType.BUNGEE);
                    BedWars.debug(e.getPlayer().getName() + " was removed from " + a.getDisplayName() + " because he was teleported outside the arena.");
                }
            }
        }
    }
}
