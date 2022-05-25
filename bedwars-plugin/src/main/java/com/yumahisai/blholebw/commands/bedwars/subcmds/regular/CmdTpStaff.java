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

import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class CmdTpStaff extends SubCommand {

    public CmdTpStaff(ParentCommand parent, String name) {
        super(parent, name);
        setPermission("blbw.tp");
        showInList(false);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) return true;
        Player p2 = (Player) s;
        if (args.length != 1) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_USAGE));
            return true;
        }

        if (!hasPermission(p2)) {
            p2.sendMessage(getMsg(p2, Messages.COMMAND_FORCESTART_NO_PERM));
            return true;
        }

        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_PLAYER_NOT_FOUND));
            return true;
        }
        IArena a = Arena.getArenaByPlayer(p);
        IArena a2 = Arena.getArenaByPlayer(p2);
        if (a == null) {
            s.sendMessage(Language.getMsg(p2, Messages.COMMAND_TP_NOT_IN_ARENA));
            return true;
        }

        if (a.getStatus() == GameState.playing) {
            if (a2 != null) {
                if (a2.isPlayer(p2)) a2.removePlayer(p2, false);
                if (a2.isSpectator(p2)) {
                    if (a2.getArenaName().equals(a.getArenaName())) {
                        p2.teleport(p);
                        return true;
                    } else a2.removeSpectator(p2, false);
                }
            }
            a.addSpectator(p2, false, p.getLocation());
        } else {
            s.sendMessage(Language.getMsg(((Player) s), Messages.COMMAND_TP_NOT_STARTED));
        }

        return true;
    }

    @Override
    public List<String> getTabComplete() {
        List<String> players = new ArrayList<>();
        for (IArena a : Arena.getArenas()) {
            for (Player p : a.getPlayers()) {
                players.add(p.getName());
            }
            for (Player p : a.getSpectators()) {
                players.add(p.getName());
            }
        }
        return players;
    }
}
