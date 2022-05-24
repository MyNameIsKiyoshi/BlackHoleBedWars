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

package com.yumahisai.blholebw.levels.internal;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.events.gameplay.GameEndEvent;
import com.yumahisai.blholebw.api.events.player.PlayerBedBreakEvent;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.events.player.PlayerLeaveArenaEvent;
import com.yumahisai.blholebw.api.events.player.PlayerXpGainEvent;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.configuration.LevelsConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LevelListeners implements Listener {

    public static LevelListeners instance;

    public LevelListeners() {
        instance = this;
    }

    //create new level data on player join
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final UUID u = e.getPlayer().getUniqueId();
        // create empty level first
        new PlayerLevel(u, 1, 0);
        Bukkit.getScheduler().runTaskAsynchronously(BedWars.plugin, () -> {
            //if (PlayerLevel.getLevelByPlayer(e.getPlayer().getUniqueId()) != null) return;
            Object[] levelData = BedWars.getRemoteDatabase().getLevelData(u);
            PlayerLevel.getLevelByPlayer(u).lazyLoad((Integer) levelData[0], (Integer) levelData[1]);
            //new PlayerLevel(e.getPlayer().getUniqueId(), (Integer)levelData[0], (Integer)levelData[1]);
            //Bukkit.broadcastMessage("LAZY LOAD");
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        final UUID u = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(BedWars.plugin, () -> {
            PlayerLevel pl = PlayerLevel.getLevelByPlayer(u);
            pl.destroy();
        });
    }

    @EventHandler
    public void onGameEnd(GameEndEvent e) {
        for (UUID p : e.getWinners()) {
            if (PlayerLevel.getLevelByPlayer(p) != null) {
                Player p1 = Bukkit.getPlayer(p);
                if (p1 == null) continue;
                int xpAmount = LevelsConfig.levels.getInt("xp-rewards.game-win");
                if (xpAmount > 0){
                    PlayerLevel.getLevelByPlayer(p).addXp(xpAmount, PlayerXpGainEvent.XpSource.GAME_WIN);
                    p1.sendMessage(Language.getMsg(p1, Messages.XP_REWARD_WIN).replace("{xp}", String.valueOf(xpAmount)));
                }
                ITeam bwt = e.getArena().getExTeam(p1.getUniqueId());
                if (bwt != null) {
                    //noinspection deprecation
                    if (bwt.getMembersCache().size() > 1) {
                        int xpAmountPerTmt = LevelsConfig.levels.getInt("xp-rewards.per-teammate");
                        if (xpAmountPerTmt > 0){
                            int tr = xpAmountPerTmt * bwt.getMembersCache().size();
                            PlayerLevel.getLevelByPlayer(p).addXp(tr, PlayerXpGainEvent.XpSource.PER_TEAMMATE);
                            p1.sendMessage(Language.getMsg(p1, "xp-reward-per-teammate").replace("{xp}", String.valueOf(tr)));
                        }
                    }
                }
            }
        }
        for (UUID p : e.getLosers()) {
            if (PlayerLevel.getLevelByPlayer(p) != null) {
                Player p1 = Bukkit.getPlayer(p);
                if (p1 == null) continue;
                ITeam bwt = e.getArena().getExTeam(p1.getUniqueId());
                if (bwt != null) {
                    //noinspection deprecation
                    if (bwt.getMembersCache().size() > 1) {
                        //noinspection deprecation
                        int xpAmountPerTmt = LevelsConfig.levels.getInt("xp-rewards.per-teammate");
                        if (xpAmountPerTmt > 0){
                            int tr = LevelsConfig.levels.getInt("xp-rewards.per-teammate") * bwt.getMembersCache().size();
                            PlayerLevel.getLevelByPlayer(p).addXp(tr, PlayerXpGainEvent.XpSource.PER_TEAMMATE);
                            p1.sendMessage(Language.getMsg(p1, Messages.XP_REWARD_PER_TEAMMATE).replace("{xp}", String.valueOf(tr)));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        final UUID u = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(BedWars.plugin, () -> {
            PlayerLevel pl = PlayerLevel.getLevelByPlayer(u);
            if (pl != null) pl.updateDatabase();
        });
    }

    @EventHandler
    public void onBreakBed(PlayerBedBreakEvent e) {
        Player player = e.getPlayer ();
        if (player == null) {
            return;
        }
        int beddestroy = LevelsConfig.levels.getInt("xp-rewards.bed-destroyed");
        if (beddestroy > 0) {
            PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(beddestroy, PlayerXpGainEvent.XpSource.BED_DESTROYED);
            player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_BED_DESTROY).replace("{xp}", String.valueOf(beddestroy)));
        }
    }

    @EventHandler
    public void onKill(PlayerKillEvent e) {
        Player player = e.getKiller ();
        Player victim = e.getVictim ();
        if (player == null || victim.equals(player)) {
            return;
        }
        int finalkill = LevelsConfig.levels.getInt("xp-rewards.final-kill");
        int regularkill = LevelsConfig.levels.getInt("xp-rewards.regular-kill");
        if (e.getCause ().isFinalKill ()) {
            if (finalkill > 0) {
                PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(finalkill, PlayerXpGainEvent.XpSource.FINAL_KILL);
                player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_FINAL_KILL).replace("{xp}", String.valueOf(finalkill)));
            }
        } else {
            if (regularkill > 0) {
                PlayerLevel.getLevelByPlayer(player.getUniqueId()).addXp(regularkill, PlayerXpGainEvent.XpSource.REGULAR_KILL);
                player.sendMessage(Language.getMsg(player, Messages.XP_REWARD_REGULAR_KILL).replace("{xp}", String.valueOf(regularkill)));
            }
        }
    }
}
