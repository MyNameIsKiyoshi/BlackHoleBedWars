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

package com.yumahisai.blholebw.arena.team;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.arena.team.ITeamAssigner;
import com.yumahisai.blholebw.api.events.gameplay.TeamAssignEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class TeamAssigner implements ITeamAssigner {

    private final LinkedList<Player> skip = new LinkedList<>();

    public void assignTeams(IArena arena) {

        // team up parties first
        if (arena.getPlayers().size() > arena.getMaxInTeam() && arena.getMaxInTeam() > 1) {
            LinkedList<List<Player>> teams = new LinkedList<>();

            List<Player> members;
            for (Player player : arena.getPlayers()) {
                members = BedWars.getParty().getMembers(player);
                if (members == null) continue;
                members = new ArrayList<>(members);
                if (members.isEmpty()) continue;
                members.removeIf(member -> !arena.isPlayer(member));
                if (members.isEmpty()) continue;
                teams.add(members);
            }
            // prioritize bigger teams

            if (!teams.isEmpty()) {
                for (ITeam team : arena.getTeams()) {
                    // sort
                    teams.sort(Comparator.comparingInt(List::size));
                    if (teams.get(0).isEmpty()) break;
                    for (int i = 0; i < arena.getMaxInTeam() && team.getMembers().size() < arena.getMaxInTeam(); i++) {
                        if (teams.get(0).size() > i) {
                            Player toAdd = teams.get(0).remove(0);
                            TeamAssignEvent e = new TeamAssignEvent(toAdd, team, arena);
                            Bukkit.getPluginManager().callEvent(e);
                            if (!e.isCancelled()) {
                                toAdd.closeInventory();
                                team.addPlayers(toAdd);
                                skip.add(toAdd);
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        for (Player remaining : arena.getPlayers()) {
            if (skip.contains(remaining)) continue;
            for (ITeam team : arena.getTeams()) {
                if (team.getMembers().size() < arena.getMaxInTeam()) {
                    TeamAssignEvent e = new TeamAssignEvent(remaining, team, arena);
                    Bukkit.getPluginManager().callEvent(e);
                    if (!e.isCancelled()) {
                        remaining.closeInventory();
                        team.addPlayers(remaining);
                    }
                    break;
                }
            }
        }
    }
}
