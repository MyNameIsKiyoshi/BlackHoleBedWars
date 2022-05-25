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

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetSpawn extends SubCommand {

    public SetSpawn(ParentCommand parent, String name) {
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
            p.sendMessage(ss.getPrefix() + ChatColor.RED + "Usage: /" + BedWars.mainCmd + " setSpawn <team>");
            if (ss.getConfig().getYml().get("Team") != null) {
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    if (ss.getConfig().getYml().get("Team." + team + ".Spawn") == null) {
                        p.spigot().sendMessage(Misc.msgHoverClick(ss.getPrefix() + "Set spawn for: " + ss.getTeamColor(team) + team + " " + ChatColor.getLastColors(ss.getPrefix()) + "(click to set)", ChatColor.LIGHT_PURPLE + "Set spawn for " + ss.getTeamColor(team) + team, "/" + BedWars.mainCmd + " setSpawn " + team, ClickEvent.Action.RUN_COMMAND));
                    }
                }
            }
        } else {
            if (ss.getConfig().getYml().get("Team." + args[0]) == null) {
                p.sendMessage(ss.getPrefix() + ChatColor.RED + "Could not find target team: " + ChatColor.RED + args[0]);
                if (ss.getConfig().getYml().get("Team") != null) {
                    p.sendMessage(ss.getPrefix() + "Teams list: ");
                    for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                        p.spigot().sendMessage(Misc.msgHoverClick(ChatColor.LIGHT_PURPLE + " " + '▪' + " " + ss.getTeamColor(team) + team + " " + ChatColor.getLastColors(ss.getPrefix()) + "(click to set)", ChatColor.LIGHT_PURPLE + "Set spawn for " + ss.getTeamColor(team) + team, "/" + BedWars.mainCmd + " setSpawn " + team, ClickEvent.Action.RUN_COMMAND));
                    }
                }
            } else {
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Spawn") != null) {
                    com.yumahisai.blholebw.commands.Misc.removeArmorStand("spawn", ss.getConfig().getArenaLoc("Team." + args[0] + ".Spawn"), ss.getConfig().getString("Team." + args[0] + ".Spawn"));
                }
                ss.getConfig().saveArenaLoc("Team." + args[0] + ".Spawn", p.getLocation());
                String teamm = ss.getTeamColor(args[0]) + args[0];
                p.sendMessage(ChatColor.LIGHT_PURPLE + " " + '▪' + " " + "Spawn set for: " + teamm);
                com.yumahisai.blholebw.commands.Misc.createArmorStand(teamm + " " + ChatColor.LIGHT_PURPLE + "SPAWN SET", p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
                int radius = ss.getConfig().getInt(ConfigPath.ARENA_ISLAND_RADIUS);
                Location l = p.getLocation();
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            Block b = l.clone().add(x, y, z).getBlock();
                            if (BedWars.nms.isBed(b.getType())) {
                                p.teleport(b.getLocation());
                                Bukkit.dispatchCommand(p, getParent().getName() + " setBed " + args[0]);
                                return true;
                            }
                        }
                    }
                }
                if (ss.getConfig().getYml().get("Team") != null) {
                    StringBuilder remainging = new StringBuilder();
                    for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                        if (ss.getConfig().getYml().get("Team." + team + ".Spawn") == null) {
                            remainging.append(ss.getTeamColor(team)).append(team).append(" ");
                        }
                    }
                    if (remainging.toString().length() > 0) {
                        p.sendMessage(ss.getPrefix() + "Remaining: " + remainging.toString());
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<>();
    }

    @Override
    public boolean canSee(CommandSender s, com.yumahisai.blholebw.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return hasPermission(s);
    }
}
