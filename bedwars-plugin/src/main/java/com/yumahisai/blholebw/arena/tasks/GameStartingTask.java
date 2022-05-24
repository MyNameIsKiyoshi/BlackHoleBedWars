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
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.NextEvent;
import com.yumahisai.blholebw.api.arena.generator.GeneratorType;
import com.yumahisai.blholebw.api.arena.generator.IGenerator;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.tasks.StartingTask;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.team.BedWarsTeam;
import com.yumahisai.blholebw.arena.team.LegacyTeamAssigner;
import com.yumahisai.blholebw.configuration.Sounds;
import com.yumahisai.blholebw.support.papi.SupportPAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

import static com.yumahisai.blholebw.api.language.Language.getList;
import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class GameStartingTask implements Runnable, StartingTask {

    private int countdown;
    private final IArena arena;
    private final BukkitTask task;

    public GameStartingTask(Arena arena) {
        this.arena = arena;
        countdown = BedWars.config.getInt(ConfigPath.GENERAL_CONFIGURATION_START_COUNTDOWN_REGULAR);
        task = Bukkit.getScheduler().runTaskTimer(BedWars.plugin, this, 0, 20L);
    }


    /**
     * Get countdown value
     */
    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    /**
     * Get arena
     */
    public IArena getArena() {
        return arena;
    }

    /**
     * Get task ID
     */
    public int getTask() {
        return task.getTaskId();
    }

    @Override
    public BukkitTask getBukkitTask() {
        return task;
    }

    @Override
    public void run() {
        if (countdown == 0) {
            if (BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_EXPERIMENTAL_TEAM_ASSIGNER)) {
                getArena().getTeamAssigner().assignTeams(getArena());
            } else {
                LegacyTeamAssigner.assignTeams(getArena());
            }


            //Color bed block if possible
            //Destroy bed if team is empty
            //Spawn shops and upgrades
            //Disable generators for empty teams if required
            for (ITeam team : getArena().getTeams()) {
                BedWars.nms.colorBed(team);
                if (team.getMembers().isEmpty()) {
                    team.setBedDestroyed(true);
                    if (getArena().getConfig().getBoolean(ConfigPath.ARENA_DISABLE_GENERATOR_FOR_EMPTY_TEAMS)) {
                        for (IGenerator gen : team.getGenerators()) {
                            gen.disable();
                        }
                    }
                }
            }

            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
                //Enable diamond/ emerald generators
                for (IGenerator og : getArena().getOreGenerators()) {
                    if (og.getType() == GeneratorType.EMERALD || og.getType() == GeneratorType.DIAMOND)
                        og.enableRotation();
                }
            }, 60L);

            //Spawn players
            spawnPlayers();

            //Lobby removal
            BedWars.getAPI().getRestoreAdapter().onLobbyRemoval(arena);


            task.cancel();
            getArena().changeStatus(GameState.playing);

            // Check if emerald should be first based on time
            if (getArena().getUpgradeDiamondsCount() < getArena().getUpgradeEmeraldsCount()) {
                getArena().setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_II);
            } else {
                getArena().setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_II);
            }

            //Spawn shopkeepers
            for (ITeam bwt : getArena().getTeams()) {
                bwt.spawnNPCs();
            }
            return;
        }

        //Send countdown
        if (getCountdown() % 10 == 0 || getCountdown() <= 5) {
            if (getCountdown() < 5) {
                Sounds.playSound(ConfigPath.SOUNDS_COUNTDOWN_TICK_X + getCountdown(), getArena().getPlayers());
            } else {
                Sounds.playSound(ConfigPath.SOUNDS_COUNTDOWN_TICK, getArena().getPlayers());
            }
            for (Player player : getArena().getPlayers()) {
                Language playerLang = Language.getPlayerLanguage(player);
                String[] titleSubtitle = Language.getCountDownTitle(playerLang, getCountdown());
                BedWars.nms.sendTitle(player, titleSubtitle[0], titleSubtitle[1], 0, 20, 10);
                player.sendMessage(getMsg(player, Messages.ARENA_STATUS_START_COUNTDOWN_CHAT).replace("{time}", String.valueOf(getCountdown())));
            }
        }
        countdown--;
    }

    //Spawn players
    private void spawnPlayers() {
        for (ITeam bwt : getArena().getTeams()) {
            for (Player p : new ArrayList<>(bwt.getMembers())) {
                BedWarsTeam.reSpawnInvulnerability.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
                bwt.firstSpawn(p);
                Sounds.playSound(ConfigPath.SOUND_GAME_START, p);
                BedWars.nms.sendTitle(p, getMsg(p, Messages.ARENA_STATUS_START_PLAYER_TITLE), null, 0, 30, 10);
                for (String tut : getList(p, Messages.ARENA_STATUS_START_PLAYER_TUTORIAL)) {
                    p.sendMessage(SupportPAPI.getSupportPAPI().replace(p, tut));
                }
            }
        }
    }

    public void cancel() {
        task.cancel();
    }
}
