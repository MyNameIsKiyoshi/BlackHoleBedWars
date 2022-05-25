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

package com.yumahisai.blholebw.commands.bedwars.subcmds.regular;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Sounds;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class CmdJoin extends SubCommand {

    public CmdJoin(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(19);
        showInList(false);
        setDisplayInfo(MainCommand.createTC("§d ▪ §7/"+ MainCommand.getInstance().getName()+" join §d<random/ arena/ groupName>",
                "/"+getParent().getName()+" "+getSubCommandName(), "§fJoin an arena by name or by group.\n§f/blbw join random - join random arena."));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        if (args.length < 1) {
            s.sendMessage(getMsg(p, Messages.COMMAND_JOIN_USAGE));
            return true;
        }
        if (args[0].equalsIgnoreCase("random")){
            if (!Arena.joinRandomArena(p)){
                s.sendMessage(getMsg(p, Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", p);
            } else {
                Sounds.playSound("join-allowed", p);
            }
            return true;
        }
        if (MainCommand.isArenaGroup(args[0]) || args[0].contains("+")) {
            if (!Arena.joinRandomFromGroup(p, args[0])) {
                s.sendMessage(getMsg(p, Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", p);
            } else {
                Sounds.playSound("join-allowed", p);
            }
            return true;
        } else if (Arena.getArenaByName(args[0]) != null) {
            if (Arena.getArenaByName(args[0]).addPlayer(p, false)){
                Sounds.playSound("join-allowed", p);
            } else {
                Sounds.playSound("join-denied", p);
            }
            return true;
        } else if (Arena.getArenaByIdentifier(args[0]) != null) {
            if (Arena.getArenaByIdentifier(args[0]).addPlayer(p, false)){
                Sounds.playSound("join-allowed", p);
            } else {
                Sounds.playSound("join-denied", p);
            }
            return true;
        }
        s.sendMessage(getMsg(p, Messages.COMMAND_JOIN_GROUP_OR_ARENA_NOT_FOUND).replace("{name}", args[0]));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        List<String> tab = new ArrayList<>(BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_ARENA_GROUPS));
        for (IArena arena : Arena.getArenas()){
            tab.add(arena.getArenaName());
        }
        return tab;
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
