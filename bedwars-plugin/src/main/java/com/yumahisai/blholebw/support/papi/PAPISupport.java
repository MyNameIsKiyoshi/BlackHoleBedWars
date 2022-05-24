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

package com.yumahisai.blholebw.support.papi;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.commands.shout.ShoutCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class PAPISupport extends PlaceholderExpansion {

    private static final SimpleDateFormat elapsedFormat = new SimpleDateFormat("HH:mm");

    @NotNull
    @Override
    public String getIdentifier() {
        return "blholebw";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "YumaHisai";
    }

    @NotNull
    @Override
    public String getVersion() {
        return BedWars.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if (s == null) return null;

        if (s.startsWith("arena_status_")) {
            IArena a = Arena.getArenaByName(s.replace("arena_status_", ""));
            if (a == null) {
                return player == null ? Language.getDefaultLanguage().m(Messages.ARENA_STATUS_RESTARTING_NAME) :
                        Language.getMsg(player, Messages.ARENA_STATUS_RESTARTING_NAME);
            }
            return a.getDisplayStatus(Language.getDefaultLanguage());
        }

        if (s.startsWith("arena_count_")) {
            int players = 0;

            String[] arenas = s.replace("arena_count_", "").split("\\+");
            IArena a;
            for (String arena : arenas) {
                a = Arena.getArenaByName(arena);
                if (a != null) {
                    players += a.getPlayers().size();
                }
            }

            return String.valueOf(players);
        }

        if (s.startsWith("group_count_")) {
            return String.valueOf(Arena.getPlayers(s.replace("group_count_", "")));
        }

        if (s.startsWith("arena_group_")) {
            String a = s.replace("arena_group_", "");
            IArena arena = Arena.getArenaByName(a);
            if (arena != null) {
                return arena.getGroup();
            }
            return "-";
        }

        if (player == null) return null;
        String replay = "";
        IArena a = Arena.getArenaByPlayer(player);
        switch (s) {
            case "stats_firstplay":
                Instant firstPlay = BedWars.getStatsManager().get(player.getUniqueId()).getFirstPlay();
                replay = new SimpleDateFormat(getMsg(player, Messages.FORMATTING_STATS_DATE_FORMAT)).format(firstPlay != null ? Timestamp.from(firstPlay) : null);
                break;
            case "stats_lastplay":
                Instant lastPlay = BedWars.getStatsManager().get(player.getUniqueId()).getLastPlay();
                replay = new SimpleDateFormat(getMsg(player, Messages.FORMATTING_STATS_DATE_FORMAT)).format(lastPlay != null ? Timestamp.from(lastPlay) : null);
                break;
            case "stats_total_kills":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getTotalKills());
                break;
            case "stats_kills":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getKills());
                break;
            case "stats_wins":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getWins());
                break;
            case "stats_finalkills":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getFinalKills());
                break;
            case "stats_deaths":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getDeaths());
                break;
            case "stats_losses":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getLosses());
                break;
            case "stats_finaldeaths":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getFinalDeaths());
                break;
            case "stats_bedsdestroyed":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getBedsDestroyed());
                break;
            case "stats_gamesplayed":
                replay = String.valueOf(BedWars.getStatsManager().get(player.getUniqueId()).getGamesPlayed());
                break;
            case "current_online":
                replay = String.valueOf(Arena.getArenaByPlayer().size());
                break;
            case "current_arenas":
                replay = String.valueOf(Arena.getArenas().size());
                break;
            case "current_playing":
                if (a != null) {
                    replay = String.valueOf(a.getPlayers().size());
                }
                break;
            case "player_team_color":
                if (a != null && a.isPlayer(player) && a.getStatus() == GameState.playing) {
                    ITeam team = a.getTeam(player);
                    if (team != null) {
                        replay += String.valueOf(team.getColor().chat());
                    }
                }
                break;
            case "player_team":
                if (a != null) {
                    if (ShoutCommand.isShout(player)) {
                        replay += Language.getMsg(player, Messages.FORMAT_PAPI_PLAYER_TEAM_SHOUT);
                    }
                    if (a.isPlayer(player)) {
                        if (a.getStatus() == GameState.playing) {
                            ITeam bwt = a.getTeam(player);
                            if (bwt != null) {
                                replay += Language.getMsg(player, Messages.FORMAT_PAPI_PLAYER_TEAM_TEAM).replace("{TeamName}",
                                        bwt.getDisplayName(Language.getPlayerLanguage(player))).replace("{TeamColor}", String.valueOf(bwt.getColor().chat()));
                            }
                        }
                    } else {
                        replay += Language.getMsg(player, Messages.FORMAT_PAPI_PLAYER_TEAM_SPECTATOR);
                    }
                }
                break;
            case "player_level":
                replay = BedWars.getLevelSupport().getLevel(player);
                break;
            case "player_level_raw":
                replay = String.valueOf(BedWars.getLevelSupport().getPlayerLevel(player));
                break;
            case "player_progress":
                replay = BedWars.getLevelSupport().getProgressBar(player);
                break;
            case "player_xp_formatted":
                replay = BedWars.getLevelSupport().getCurrentXpFormatted(player);
                break;
            case "player_xp":
                replay = String.valueOf(BedWars.getLevelSupport().getCurrentXp(player));
                break;
            case "player_rerq_xp_formatted":
                replay = BedWars.getLevelSupport().getRequiredXpFormatted(player);
                break;
            case "player_rerq_xp":
                replay = String.valueOf(BedWars.getLevelSupport().getRequiredXp(player));
                break;
            case "player_status":
                if(a != null) {
                    switch (a.getStatus()) {
                        case waiting:
                        case starting:
                            replay = "WAITING";
                            break;
                        case playing:
                            if(a.isPlayer(player)) {
                                replay = "PLAYING";
                            } else if(a.isSpectator(player)) {
                                replay = "SPECTATING";
                            } else {
                                replay = "IN_GAME_BUT_NOT"; // this shouldnt happen
                            }
                            break;
                        case restarting:
                            replay = "RESTARTING";
                            break;
                    }
                } else {
                    replay = "NONE";
                }
                break;
            case "current_arena_group":
                if (a != null) {
                    replay = a.getGroup();
                }
                break;
            case "elapsed_time":
                if (a != null) {
                    replay = elapsedFormat.format(Instant.now().minusMillis(a.getStartTime().toEpochMilli()));
                }
                break;

        }
        return replay;
    }
}
