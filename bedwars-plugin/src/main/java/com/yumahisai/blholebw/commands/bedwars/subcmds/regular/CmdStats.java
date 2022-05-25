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

import com.yumahisai.blholebw.api.BedWars;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CmdStats extends SubCommand {

    public CmdStats(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(16);
        showInList(false);
        setDisplayInfo(MainCommand.createTC("§d ▪ §7/"+ MainCommand.getInstance().getName()+" "+getSubCommandName(), "/"+getParent().getName()+" "+getSubCommandName(), "§fOpens the stats GUI."));
    }

    private static ConcurrentHashMap<UUID, Long> statsCoolDown = new ConcurrentHashMap<>();

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null){
            if (!(a.getStatus() == GameState.starting || a.getStatus() == GameState.waiting)){
                if (!a.isSpectator(p)){
                    return false;
                }
            }
        }
        if (statsCoolDown.containsKey(p.getUniqueId())){
            if (System.currentTimeMillis() - 3000 >= statsCoolDown.get(p.getUniqueId())) {
                statsCoolDown.replace(p.getUniqueId(), System.currentTimeMillis());
            } else {
                //wait 3 seconds
                return true;
            }
        } else {
            statsCoolDown.put(p.getUniqueId(), System.currentTimeMillis());
        }
        Misc.openStatsGUI(p);
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
        if (Arena.isInArena(p)) return false;

        if (SetupSession.isInSetupSession(p.getUniqueId())) return false;
        return hasPermission(s);
    }

    public static ConcurrentHashMap<UUID, Long> getStatsCoolDown() {
        return statsCoolDown;
    }
}
