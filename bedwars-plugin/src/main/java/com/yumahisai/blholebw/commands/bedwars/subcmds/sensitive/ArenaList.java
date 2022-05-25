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

import com.yumahisai.blholebw.api.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.command.SubCommand;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.bedwars.MainCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaList extends SubCommand {

    private static final int ARENAS_PER_PAGE = 10;

    public ArenaList(ParentCommand parent, String name) {
        super(parent, name);
        setPriority(3);
        showInList(true);
        setDisplayInfo(Misc.msgHoverClick("§d ▪ §7/" + MainCommand.getInstance().getName() + " " + getSubCommandName() + ((getArenas().size() == 0) ? " §c(0 set)" : " §a(" + getArenas().size() + " set)"),
                "§fShow available arenas", "/" + MainCommand.getInstance().getName() + " " + getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) return false;
        Player p = (Player) s;


        int page = 1;
        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 0) {
                    page = 1;
                }
            } catch (Exception ignored) {
            }
        }
        int start = (page - 1) * ARENAS_PER_PAGE;
        List<IArena> arenas = new ArrayList<>(Arena.getArenas());
        if (arenas.size() <= start){
            page = 1;
            start = 0;
        }

        p.sendMessage(color(" \n&1|| &3" + com.yumahisai.blholebw.BedWars.plugin.getName() + "&7 Instantiated games: \n "));

        if (arenas.isEmpty()) {
            p.sendMessage(ChatColor.RED + "No arenas to display.");
            return true;
        }

        int limit = Math.min(arenas.size(), start + ARENAS_PER_PAGE);

        arenas.subList(start, limit).forEach(arena -> {
            String gameState = arena.getDisplayStatus(Language.getPlayerLanguage(p));
            String msg = color(
                    "ID: &d" + arena.getWorldName() +
                            " &fG: &d" + arena.getDisplayGroup(p) +
                            " &fP: &d" + (arena.getPlayers().size() + arena.getSpectators().size()) +
                            " &fS: " + gameState +
                            " &fWl: &d" + (Bukkit.getWorld(arena.getWorldName()) != null)
            );


            p.sendMessage(msg);
        });

        p.sendMessage(" ");

        if (arenas.size() > ARENAS_PER_PAGE * page) {
            p.sendMessage(ChatColor.GRAY + "Type /" + ChatColor.GREEN + MainCommand.getInstance().getName() + " arenaList " + ++page + ChatColor.GRAY + " for next page.");
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @NotNull
    private java.util.List<String> getArenas() {
        ArrayList<String> arene = new ArrayList<>();
        File dir = new File(com.yumahisai.blholebw.BedWars.plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (f.isFile()) {
                    if (f.getName().contains(".yml")) {
                        arene.add(f.getName().replace(".yml", ""));
                    }
                }
            }
        }
        return arene;
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {

        if (s instanceof Player) {
            Player p = (Player) s;
            if (Arena.isInArena(p)) return false;

            if (SetupSession.isInSetupSession(p.getUniqueId())) return false;
        }

        return hasPermission(s);
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
