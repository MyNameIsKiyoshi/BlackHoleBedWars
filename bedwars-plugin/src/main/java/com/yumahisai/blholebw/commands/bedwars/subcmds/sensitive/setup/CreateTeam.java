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
import com.yumahisai.blholebw.api.server.SetupType;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateTeam extends SubCommand {

    public CreateTeam(ParentCommand parent, String name) {
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
            s.sendMessage("§c ▪ §7You're not in a setup session!");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§c▪ §7Usage: /" + com.yumahisai.blholebw.BedWars.mainCmd + " createTeam §o<name> §o<color>");
            StringBuilder colors = new StringBuilder("§7");
            for (TeamColor t : TeamColor.values()) {
                colors.append(t.chat()).append(t.toString()).append(ChatColor.GRAY).append(", ");
            }
            colors = new StringBuilder(colors.toString().substring(0, colors.toString().length() - 2) + ChatColor.GRAY + ".");
            p.sendMessage("§d ▪ §7Available colors: " + colors);
        } else {
            boolean y = true;
            for (TeamColor t : TeamColor.values()) {
                if (t.toString().equalsIgnoreCase(args[1])) {
                    y = false;
                }
            }
            if (y) {
                p.sendMessage("§c▪ §7Invalid color!");
                StringBuilder colors = new StringBuilder("§7");
                for (TeamColor t : TeamColor.values()) {
                    colors.append(t.chat()).append(t.toString()).append(ChatColor.GRAY).append(", ");
                }
                colors = new StringBuilder(colors.toString().substring(0, colors.toString().length() - 2) + ChatColor.GRAY + ".");
                p.sendMessage("§d ▪ §7Available colors: " + colors);
            } else {
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Color") != null) {
                    p.sendMessage("§c▪ §7" + args[0] + " team already exists!");
                    return true;
                }
                ss.getConfig().set("Team." + args[0] + ".Color", args[1].toUpperCase());
                p.sendMessage("§d ▪ §7" + TeamColor.getChatColor(args[1]) + args[0] + " §7created!");
                if (ss.getSetupType() == SetupType.ASSISTED) {
                    ss.getConfig().reload();
                    int teams = ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false).size();
                    int max = 1;
                    if (teams == 4) {
                        max = 2;
                    }
                    ss.getConfig().set("maxInTeam", max);
                }
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
