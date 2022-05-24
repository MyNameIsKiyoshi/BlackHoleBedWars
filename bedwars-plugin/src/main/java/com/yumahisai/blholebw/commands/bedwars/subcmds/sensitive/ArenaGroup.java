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

package com.yumahisai.blholebw.commands.bedwars.subcmds.sensitive;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import com.yumahisai.blholebw.configuration.ArenaConfig;
import com.yumahisai.blholebw.configuration.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaGroup extends SubCommand {

    public ArenaGroup(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(8);
        showInList(true);
        setPermission(Permissions.PERMISSION_ARENA_GROUP);
        setDisplayInfo(Misc.msgHoverClick("§6 ▪ §7/" + getParent().getName() + " " + getSubCommandName()+" §8- §eclick for details", "§fManage arena groups.",
                "/" + getParent().getName() + " " + getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        if (!MainCommand.isLobbySet(p)) return true;
        if (args.length < 2 && (args.length < 1 || !args[0].equalsIgnoreCase("list"))) {
            sendArenaGroupCmdList(p);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args[0].contains("+")) {
                p.sendMessage("§c▪ §7" + args[0] + " mustn't contain this symbol: " + ChatColor.RED + "+");
                return true;
            }
            java.util.List<String> groups;
            if (BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS) == null) {
                groups = new ArrayList<>();
            } else {
                groups = BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS);
            }
            if (groups.contains(args[1])) {
                p.sendMessage("§c▪ §7This group already exists!");
                return true;
            }
            groups.add(args[1]);
            BedWars.config.set(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS, groups);
            p.sendMessage("§6 ▪ §7Group created!");
        } else if (args[0].equalsIgnoreCase("remove")) {
            List<String> groups;
            if (BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS) == null) {
                groups = new ArrayList<>();
            } else {
                groups = BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS);
            }
            if (!groups.contains(args[1])) {
                p.sendMessage("§c▪ §7This group doesn't exist!");
                return true;
            }
            groups.remove(args[1]);
            BedWars.config.set(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS, groups);
            p.sendMessage("§6 ▪ §7Group deleted!");
        } else if (args[0].equalsIgnoreCase("list")) {
            List<String> groups;
            if (BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS) == null) {
                groups = new ArrayList<>();
            } else {
                groups = BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS);
            }
            p.sendMessage("§7Available arena groups:");
            p.sendMessage("§6 ▪ §fDefault");
            for (String gs : groups) {
                p.sendMessage("§6 ▪ §f" + gs);
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sendArenaGroupCmdList(p);
                return true;
            }
            if (BedWars.config.getYml().get(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS) != null) {
                if (BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS).contains(args[2])) {
                    File arena = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[1] + ".yml");
                    if (!arena.exists()) {
                        p.sendMessage("§c▪ §7Arena " + args[1] + " doesn't exist!");
                        return true;
                    }
                    ArenaConfig cm = new ArenaConfig(BedWars.plugin, args[1], BedWars.plugin.getDataFolder().getPath() + "/Arenas");
                    cm.set("group", args[2]);
                    if (Arena.getArenaByName(args[1]) != null) {
                        Arena.getArenaByName(args[1]).setGroup(args[2]);
                    }
                    p.sendMessage("§6 ▪ §7" + args[1] + " was added to the group: " + args[2]);
                } else {
                    p.sendMessage("§6 ▪ §7There isn't any group called: " + args[2]);
                    Bukkit.dispatchCommand(p, "/bw list");
                }
            } else {
                p.sendMessage("§6 ▪ §7There isn't any group called: " + args[2]);
                Bukkit.dispatchCommand(p, "/bw list");
            }
        } else {
            sendArenaGroupCmdList(p);
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("create", "remove", "list", "set");
    }

    private void sendArenaGroupCmdList(Player p) {
        p.spigot().sendMessage(Misc.msgHoverClick("§6 ▪ §7/" + getParent().getName() + " " + getSubCommandName() + " create §o<groupName>",
                "Create an arena group. More details on our wiki.", "/" + getParent().getName() + " " + getSubCommandName() + " create",
                ClickEvent.Action.SUGGEST_COMMAND));
        p.spigot().sendMessage(Misc.msgHoverClick("§6 ▪ §7/" + getParent().getName() + " " + getSubCommandName() + " list",
                "View available groups.", "/" + getParent().getName() + " " + getSubCommandName() + " list",
                ClickEvent.Action.RUN_COMMAND));
        p.spigot().sendMessage(Misc.msgHoverClick("§6 ▪ §7/" + getParent().getName() + " " + getSubCommandName() + " remove §o<groupName>",
                "Remove an arena group. More details on our wiki.", "/" + getParent().getName() + " " + getSubCommandName() + " remove",
                ClickEvent.Action.SUGGEST_COMMAND));
        p.spigot().sendMessage(Misc.msgHoverClick("§6 ▪ §7/" + getParent().getName() + " " + getSubCommandName() + " §r§7set §o<arenaName> <groupName>",
                "Set the arena group. More details on our wiki.", "/" + getParent().getName() + " " + getSubCommandName() + " set",
                ClickEvent.Action.SUGGEST_COMMAND));
    }

    @Override
    public boolean canSee(CommandSender s, com.yumahisai.blholebw.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (Arena.isInArena(p)) return false;

        if (SetupSession.isInSetupSession(p.getUniqueId())) return false;
        return hasPermission(s);
    }
}
