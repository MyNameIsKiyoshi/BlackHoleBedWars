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
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.configuration.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetBuildHeight extends SubCommand {
    /**
     * Create a sub-command for a bedWars command
     * Make sure you return true or it will say command not found
     *
     * @param parent parent command
     * @param name   sub-command name
     */
    public SetBuildHeight(ParentCommand parent, String name) {
        super(parent, name);
        setArenaSetupCommand(true);
        setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    /**
     * Add your sub-command code under this method
     *
     * @param args
     * @param s
     */
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
            p.sendMessage("§c▪ §7Usage: /" + com.yumahisai.blholebw.BedWars.mainCmd + " setMaxBuildHeight <int>");
        } else {
            try {
                Integer.parseInt(args[0]);
            } catch (Exception ex) {
                p.sendMessage("§c▪ §7Usage: /" + com.yumahisai.blholebw.BedWars.mainCmd + " setMaxBuildHeight <int>");
                return true;
            }
            ss.getConfig().set("max-build-y", Integer.valueOf(args[0]));
            p.sendMessage("§d ▪ §7Max build height Y set to §d" + args[0] + "§7!");
        }
        return true;
    }

    /**
     * Manage sub-command tab complete
     */
    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("180", "256");
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {
        if (s instanceof ConsoleCommandSender) return false;

        Player p = (Player) s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return false;

        return hasPermission(s);
    }
}
