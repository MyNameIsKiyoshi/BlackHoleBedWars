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
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class WaitingPos extends SubCommand {

    public WaitingPos(ParentCommand parent, String name) {
        super(parent, name);
        setArenaSetupCommand(true);
        setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null){
            s.sendMessage("§c ▪ §7You're not in a setup session!");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("§c▪ §7Usage: /" + BedWars.mainCmd + " "+getSubCommandName()+" 1 or 2");
        } else {
            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2")) {
                p.sendMessage("§d ▪ §7Pos " + args[0] + " set!");
                ss.getConfig().saveArenaLoc("waiting.Pos" + args[0], p.getLocation());
                ss.getConfig().reload();
                if (ss.getConfig().getYml().get("waiting.Pos1") == null){
                    p.sendMessage("§c ▪ §7Set the remaining position:");
                    p.spigot().sendMessage(Misc.msgHoverClick("§c ▪ §7/"+ BedWars.mainCmd+" waitingPos 1", "§dSet pos 1", "/"+getParent().getName()+" waitingPos 1", ClickEvent.Action.RUN_COMMAND));
                } else if (ss.getConfig().getYml().get("waiting.Pos2") == null){
                    p.sendMessage("§c ▪ §7Set the remaining position:");
                    p.spigot().sendMessage(Misc.msgHoverClick("§c ▪ §7/"+ BedWars.mainCmd+" waitingPos 2", "§dSet pos 2", "/"+getParent().getName()+" waitingPos 2", ClickEvent.Action.RUN_COMMAND));
                }
            } else {
                p.sendMessage("§c▪ §7Usage: /" + BedWars.mainCmd + " "+getSubCommandName()+" 1 or 2");
            }
        }
        if (!((ss.getConfig().getYml().get("waiting.Pos1") == null || ss.getConfig().getYml().get("waiting.Pos2") == null))){
            Bukkit.dispatchCommand(p, BedWars.mainCmd+" cmds");
            s.sendMessage("§d ▪ §7Set teams spawn if you didn't!");
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("1", "2");
    }

    @Override
    public boolean canSee(CommandSender s, com.yumahisai.blholebw.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return hasPermission(s);
    }
}
