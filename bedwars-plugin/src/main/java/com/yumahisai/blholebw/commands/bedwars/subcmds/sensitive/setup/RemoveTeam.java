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
import com.yumahisai.blholebw.api.arena.team.TeamColor;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Permissions;
import com.yumahisai.blholebw.configuration.Sounds;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class RemoveTeam extends SubCommand {

    public RemoveTeam(ParentCommand parent, String name) {
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
            //s.sendMessage("§c ▪ §7You're not in a setup session!");
            return false;
        }
        if (args.length < 1) {
            p.sendMessage(ss.getPrefix() + ChatColor.RED + "Usage: /" + com.yumahisai.blholebw.BedWars.mainCmd + " removeTeam <teamName>");
            if (ss.getConfig().getYml().get("Team") != null) {
                p.sendMessage(ss.getPrefix() + "Available teams: ");
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    p.spigot().sendMessage(Misc.msgHoverClick(ChatColor.DARK_PURPLE + " " + '▪' + " " + TeamColor.getChatColor(team) + team, ChatColor.GRAY + "Remove " + TeamColor.getChatColor(team) + team + " " + ChatColor.GRAY + "(click to remove)", "/" + com.yumahisai.blholebw.BedWars.mainCmd + " removeTeam " + team, ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else {
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Color") == null) {
                p.sendMessage(ss.getPrefix() + "This team doesn't exist: " + args[0]);
                com.yumahisai.blholebw.BedWars.nms.sendTitle(p, " ", ChatColor.RED + "Team not found: " + args[0], 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_INSUFF_MONEY, p);
            } else {
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Iron") != null) {
                    for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Iron")) {
                        com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, loc, null);
                    }
                }
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Gold") != null) {
                    for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Gold")) {
                        com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, loc, null);
                    }
                }
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Emerald") != null) {
                    for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Emerald")) {
                        com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, loc, null);
                    }
                }
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Shop") != null) {
                    com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + ".Shop"), null);
                }
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Upgrade") != null) {
                    com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + ".Upgrade"), null);
                }
                if (ss.getConfig().getYml().get("Team." + args[0] + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC) != null) {
                    com.yumahisai.blholebw.commands.Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + "." + ConfigPath.ARENA_TEAM_KILL_DROPS_LOC), null);
                }
                p.sendMessage(ss.getPrefix() + "Team removed: " + ss.getTeamColor(args[0]) + args[0]);
                com.yumahisai.blholebw.BedWars.nms.sendTitle(p, " ", ChatColor.GREEN + "Team removed: " + ss.getTeamColor(args[0]) + args[0], 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, p);
                ss.getConfig().set("Team." + args[0], null);
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return hasPermission(s);
    }
}
