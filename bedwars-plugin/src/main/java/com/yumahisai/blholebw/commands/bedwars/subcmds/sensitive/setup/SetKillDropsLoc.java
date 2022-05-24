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

package com.yumahisai.blholebw.commands.bedwars.subcmds.sensitive.setup;

import com.yumahisai.blholebw.api.BedWars;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.server.SetupType;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.Misc;
import com.yumahisai.blholebw.configuration.ArenaConfig;
import com.yumahisai.blholebw.configuration.Permissions;
import com.yumahisai.blholebw.configuration.Sounds;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetKillDropsLoc extends SubCommand {


    public SetKillDropsLoc(ParentCommand parent, String name) {
        super(parent, name);
        setArenaSetupCommand(true);
        setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) {
            //s.sendMessage(ss.getPrefix()"§c ▪ §7You're not in a setup session!");
            return false;
        }
        ArenaConfig arena = ss.getConfig();
        if (args.length < 1) {
            String foundTeam = "";
            double distance = 100;
            if (ss.getConfig().getYml().getConfigurationSection("Team") == null) {
                p.sendMessage(ss.getPrefix() + "Please create teams first!");
                com.yumahisai.blholebw.BedWars.nms.sendTitle(p, " ", ChatColor.RED + "Please create teams first!", 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_INSUFF_MONEY, p);
                return true;
            }
            for (String team : ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false)) {
                if (ss.getConfig().getYml().get("Team." + team + ".Spawn") == null) continue;
                double dis = ss.getConfig().getArenaLoc("Team." + team + ".Spawn").distance(p.getLocation());
                if (dis <= ss.getConfig().getInt(ConfigPath.ARENA_ISLAND_RADIUS)) {
                    if (dis < distance) {
                        distance = dis;
                        foundTeam = team;
                    }
                }
            }
            if (!foundTeam.isEmpty()) {
                if (ss.getConfig().getYml().get("Team." + foundTeam + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC) != null) {
                    Misc.removeArmorStand("Kill drops", ss.getConfig().getArenaLoc("Team." + foundTeam + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC), null);
                }
                arena.set("Team." + foundTeam + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC, arena.stringLocationArenaFormat(p.getLocation()));
                String team = ss.getTeamColor(foundTeam) + foundTeam;
                p.sendMessage(ss.getPrefix() + "Kill drops set for team: " + team);
                Misc.createArmorStand(ChatColor.GOLD + "Kill drops " + team, p.getLocation(), null);
                com.yumahisai.blholebw.BedWars.nms.sendTitle(p, " ", ChatColor.GREEN + "Kill drops set for team: " + team, 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, p);

                if (ss.getSetupType() == SetupType.ASSISTED) {
                    Bukkit.dispatchCommand(p, getParent().getName());
                }
                return true;
            }

            p.sendMessage(ss.getPrefix() + ChatColor.RED + "Usage: /" + com.yumahisai.blholebw.BedWars.mainCmd + " setKillDrops <teamName>");
            return true;
        }

        String foundTeam = ss.getNearestTeam();

        if (foundTeam.isEmpty()) {
            p.sendMessage("");
            p.sendMessage(ss.getPrefix() + ChatColor.RED + "Could not find any nearby team.");
            p.spigot().sendMessage(com.yumahisai.blholebw.arena.Misc.msgHoverClick(ss.getPrefix() + "Make sure you set the team's spawn first!", ChatColor.WHITE + "Set a team spawn.", "/" + getParent().getName() + " " + getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage(com.yumahisai.blholebw.arena.Misc.msgHoverClick(ss.getPrefix() + "Or if you set the spawn and it wasn't found automatically try using: /bw " + getSubCommandName() + " <team>", "Set kill drops location for a team.", "/" + getParent().getName() + " " + getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            com.yumahisai.blholebw.BedWars.nms.sendTitle(p, " ", ChatColor.RED + "Could not find any nearby team.", 5, 60, 5);
            Sounds.playSound(ConfigPath.SOUNDS_INSUFF_MONEY, p);
            return true;
        }

        if (args.length == 1) {
            if (arena.getYml().get("Team." + args[0]) != null) {
                foundTeam = args[0];
            } else {
                p.sendMessage(ss.getPrefix() + ChatColor.RED + "This team doesn't exist!");
                if (arena.getYml().get("Team") != null) {
                    p.sendMessage(ss.getPrefix() + "Available teams: ");
                    for (String team : Objects.requireNonNull(arena.getYml().getConfigurationSection("Team")).getKeys(false)) {
                        p.spigot().sendMessage(com.yumahisai.blholebw.arena.Misc.msgHoverClick(ChatColor.GOLD + " " + '▪' + " " + "Kill drops " + ss.getTeamColor(team) + team + " " + ChatColor.getLastColors(ss.getPrefix()) + "(click to set)", ChatColor.WHITE + "Set Kill drops for " + ss.getTeamColor(team) + team, "/" + com.yumahisai.blholebw.BedWars.mainCmd + " setKillDrops " + team, ClickEvent.Action.RUN_COMMAND));
                    }
                }
                return true;
            }
        }

        arena.set("Team." + foundTeam + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC, arena.stringLocationArenaFormat(p.getLocation()));
        p.sendMessage(ss.getPrefix() + "Kill drops set for: " + ss.getTeamColor(foundTeam) + foundTeam);

        if (ss.getSetupType() == SetupType.ASSISTED) {
            Bukkit.dispatchCommand(p, getParent().getName());
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<>();
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return hasPermission(s);
    }
}
