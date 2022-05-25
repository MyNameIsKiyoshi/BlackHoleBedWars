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
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import com.yumahisai.blholebw.configuration.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class CmdStart extends SubCommand {

    public CmdStart(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(15);
        showInList(true);
        setDisplayInfo(MainCommand.createTC("§d ▪ §7/"+ MainCommand.getInstance().getName()+" "+getSubCommandName()+" §8 - §dforce start an arena",
                "/"+getParent().getName()+" "+getSubCommandName(), "§fForcestart an arena.\n§fPermission: §c"+ Permissions.PERMISSION_FORCESTART));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null){
            p.sendMessage(getMsg(p, Messages.COMMAND_FORCESTART_NOT_IN_GAME));
            return true;
        }
        if (!a.isPlayer(p)){
            p.sendMessage(getMsg(p, Messages.COMMAND_FORCESTART_NOT_IN_GAME));
            return true;
        }
        if (!(p.hasPermission(Permissions.PERMISSION_ALL) || p.hasPermission(Permissions.PERMISSION_FORCESTART))){
            p.sendMessage(getMsg(p, Messages.COMMAND_FORCESTART_NO_PERM));
            return true;
        }
        if (a.getStatus() == GameState.playing) return true;
        if (a.getStatus() == GameState.restarting) return true;
        if (a.getStartingTask() == null){
            if (args.length == 1 && args[0].equalsIgnoreCase("debug") && s.isOp()){
                a.changeStatus(GameState.starting);
                BedWars.debug = true;
            } else {
                return true;
            }
        }
        if (a.getStartingTask().getCountdown() < 5) return true;
        a.getStartingTask().setCountdown(5);
        p.sendMessage(getMsg(p, Messages.COMMAND_FORCESTART_SUCCESS));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, com.yumahisai.blholebw.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;

        IArena a = Arena.getArenaByPlayer(p);
        if (a != null){
            if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting){
                if (!a.isPlayer(p)) return false;
            } else {
                return false;
            }
        } else {
            return false;
        }

        if (SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return s.hasPermission(Permissions.PERMISSION_FORCESTART);
    }
}
