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
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import com.yumahisai.blholebw.configuration.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CloneArena extends SubCommand {
    public CloneArena(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(7);
        showInList(true);
        setPermission(Permissions.PERMISSION_CLONE);
        setDisplayInfo(Misc.msgHoverClick("§d ▪ §7/" + getParent().getName() + " " + getSubCommandName() + " §d<worldName> <newName>", "§fClone an existing arena.",
                "/" + getParent().getName() + " " + getSubCommandName(), ClickEvent.Action.SUGGEST_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;
        if (!MainCommand.isLobbySet(p)) return true;
        if (args.length != 2) {
            p.sendMessage("§c▪ §7Usage: §o/" + getParent().getName() + " " + getSubCommandName() + " <mapName> <newArena>");
            return true;
        }
        if (!BedWars.getAPI().getRestoreAdapter().isWorld(args[0])) {
            p.sendMessage("§c▪ §7" + args[0] + " doesn't exist!");
            return true;
        }
        File yml1 = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[0] + ".yml"), yml2 = new File(BedWars.plugin.getDataFolder(), "/Arenas/" + args[1] + ".yml");
        if (!yml1.exists()) {
            p.sendMessage("§c▪ §7" + args[0] + " doesn't exist!");
            return true;
        }
        if (BedWars.getAPI().getRestoreAdapter().isWorld(args[1]) && yml2.exists()) {
            p.sendMessage("§c▪ §7" + args[1] + " already exist!");
            return true;
        }
        if (args[1].contains("+")) {
            p.sendMessage("§c▪ §7" + args[1] + " mustn't contain this symbol: " + ChatColor.RED + "+");
            return true;
        }
        if (Arena.getArenaByName(args[0]) != null) {
            p.sendMessage("§c▪ §7Please disable " + args[0] + " first!");
            return true;
        }
        BedWars.getAPI().getRestoreAdapter().cloneArena(args[0], args[1]);
        if (yml1.exists()) {
            try {
                FileUtils.copyFile(yml1, yml2, true);
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage("§c▪ §7An error occurred while copying the map's config. Check the console.");
            }
        }
        p.sendMessage("§d ▪ §7Arena Cloned..");
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        List<String> tab = new ArrayList<>();
        File dir = new File(BedWars.plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            File[] fls = dir.listFiles();
            for (File fl : Objects.requireNonNull(fls)) {
                if (fl.isFile()) {
                    if (fl.getName().contains(".yml")) {
                        tab.add(fl.getName().replace(".yml", ""));
                    }
                }
            }
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
