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
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.ReJoin;
import com.yumahisai.blholebw.configuration.Permissions;
import com.yumahisai.blholebw.configuration.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class JoinListenerBungeeLegacy implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent e) {
        final Player p = e.getPlayer();

        // Do not allow login if the arena wasn't loaded yet
        if (Arena.getArenas().isEmpty()) {
            if (!Arena.getEnableQueue().isEmpty()) {
                e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, getMsg(e.getPlayer(), Messages.ARENA_STATUS_RESTARTING_NAME));
                return;
            }
        }

        // Check if there is an arena to rejoin
        ReJoin reJoin = ReJoin.getPlayer(p);
        if (reJoin != null) {
            // If is not allowed to rejoin
            if (!(p.hasPermission(Permissions.PERMISSION_REJOIN) || reJoin.canReJoin())) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.REJOIN_DENIED));
                reJoin.destroy(true);
            }
            // Stop here, rejoin handled. More will be handled at PlayerJoinEvent
            return;
        }

        IArena arena = Arena.getArenas().get(0);
        if (arena != null) {

            // Player logic
            if (arena.getStatus() == GameState.waiting || (arena.getStatus() == GameState.starting && arena.getStartingTask().getCountdown() > 1)) {
                // If arena is full
                if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
                    // Vip join feature
                    if (Arena.isVip(p)) {
                        boolean canJoin = false;
                        for (Player inGame : arena.getPlayers()) {
                            if (!Arena.isVip(inGame)) {
                                canJoin = true;
                                inGame.kickPlayer(getMsg(inGame, Messages.ARENA_JOIN_VIP_KICK));
                                break;
                            }
                        }
                        if (!canJoin) {
                            e.disallow(PlayerLoginEvent.Result.KICK_FULL, Language.getDefaultLanguage().m(Messages.COMMAND_JOIN_DENIED_IS_FULL_OF_VIPS));
                        }
                    } else {
                        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, getMsg(e.getPlayer(), Messages.COMMAND_JOIN_DENIED_IS_FULL));
                    }
                }
            } else if (arena.getStatus() == GameState.playing) {
                // Spectator logic
                if (!arena.isAllowSpectate()) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
                }
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Language.getDefaultLanguage().m(Messages.ARENA_STATUS_RESTARTING_NAME));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        final Player p = e.getPlayer();

        // Do not allow login if the arena wasn't loaded yet
        // I know this code is already in the login event but other plugins may allow login
        if (Arena.getArenas().isEmpty()) {
            if (!Arena.getEnableQueue().isEmpty()) {
                p.kickPlayer(getMsg(e.getPlayer(), Messages.ARENA_STATUS_RESTARTING_NAME));
                return;
            }
        }

        JoinHandlerCommon.displayCustomerDetails(p);

        if (Arena.getArenas().isEmpty()) {
            // Show setup commands if there is no arena available
            if (p.hasPermission("bw.setup")) {
                p.performCommand(BedWars.mainCmd);
            }
        } else {
            IArena arena = Arena.getArenas().get(0);
            // Add player if the game is in waiting
            if (arena.getStatus() == GameState.waiting || arena.getStatus() == GameState.starting) {
                if (arena.addPlayer(p, false)) {
                    Sounds.playSound("join-allowed", p);
                } else {
                    p.kickPlayer(getMsg(p, Messages.COMMAND_JOIN_DENIED_IS_FULL));
                }
            } else {
                // Check ReJoin
                ReJoin reJoin = ReJoin.getPlayer(p);
                if (reJoin != null) {
                    if (reJoin.canReJoin()) {
                        reJoin.reJoin(p);
                        reJoin.destroy(false);
                        return;
                    } else {
                        p.sendMessage(getMsg(p, Messages.REJOIN_DENIED));
                        reJoin.destroy(true);
                    }
                }

                // Add spectator
                if (arena.addSpectator(p, false, null)) {
                    Sounds.playSound("spectate-allowed", p);
                } else {
                    p.kickPlayer(getMsg(p, Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
                }
            }
        }
    }
}

