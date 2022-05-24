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
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.server.SetupType;
import com.yumahisai.blholebw.arena.SetupSession;
import com.yumahisai.blholebw.commands.Misc;
import com.yumahisai.blholebw.configuration.Permissions;
import com.yumahisai.blholebw.configuration.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveGenerator extends SubCommand {
    /**
     * Create a sub-command for a bedWars command
     * Make sure you return true or it will say command not found
     *
     * @param parent parent command
     * @param name   sub-command name
     */
    public RemoveGenerator(ParentCommand parent, String name) {
        super(parent, name);
        setArenaSetupCommand(true);
        setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) return false;
        Player p = (Player) s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) return false;

        if (args.length == 0) {
            String[] toRemove = new String[]{"", "", ""};
            Location nearest = null;
            if (ss.getConfig().getYml().get("Team") != null) {
                for (String team : ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false)) {
                    for (String type : new String[]{"Iron", "Gold", "Emerald"}) {
                        if (ss.getConfig().getYml().get("Team." + team + "." + type) != null) {
                            for (String loc : ss.getConfig().getList("Team." + team + "." + type)) {
                                Location loc2 = ss.getConfig().convertStringToArenaLocation(loc);
                                if (loc2 != null) {
                                    if (p.getLocation().distance(loc2) <= 2) {
                                        if (nearest != null) {
                                            if (p.getLocation().distance(nearest) > p.getLocation().distance(loc2)) {
                                                nearest = loc2;
                                                toRemove[0] = type;
                                                toRemove[1] = loc;
                                                toRemove[2] = team;
                                            }
                                        } else {
                                            nearest = loc2;
                                            toRemove[0] = type;
                                            toRemove[1] = loc;
                                            toRemove[2] = team;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (ss.getConfig().getYml().get("generator") != null) {
                for (String type : new String[]{"Emerald", "Diamond"}) {
                    if (ss.getConfig().getYml().get("generator." + type) != null) {
                        for (String loc : ss.getConfig().getList("generator." + type)) {
                            Location loc2 = ss.getConfig().convertStringToArenaLocation(loc);
                            if (loc2 == null) continue;
                            if (p.getLocation().distance(loc2) <= 2) {
                                if (nearest != null) {
                                    if (p.getLocation().distance(nearest) > p.getLocation().distance(loc2)) {
                                        nearest = loc2;
                                        toRemove[0] = type;
                                        toRemove[1] = loc;
                                        toRemove[2] = "";
                                    }
                                } else {
                                    nearest = loc2;
                                    toRemove[0] = type;
                                    toRemove[1] = loc;
                                    toRemove[2] = "";
                                }
                            }
                        }
                    }
                }
            }

            if (nearest == null) {
                p.sendMessage(ss.getPrefix() + "Could not find any nearby generator (Range 2x2).");
                p.sendMessage(ss.getPrefix() + "You mast stand close to the generator hologram's you want to remove.");
                BedWars.nms.sendTitle(p, " ", ChatColor.RED + "Could not find any nearby generator.", 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_INSUFF_MONEY, p);
                return true;
            }

            if (toRemove[2].isEmpty()) {
                List<String> list = ss.getConfig().getList("generator." + toRemove[0]);
                list.remove(toRemove[1]);

                ss.getConfig().set("generator." + toRemove[0], list);
                p.sendMessage(ss.getPrefix() + "Removed " + toRemove[0] + " generator at location: X:" + nearest.getBlockX() + " Y:" + nearest.getBlockY() + " Z:" + nearest.getZ());
                BedWars.nms.sendTitle(p, " ", ChatColor.GREEN + toRemove[0] + " generator removed.", 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, p);
                Misc.removeArmorStand(toRemove[0], nearest, toRemove[1]);
                return true;
            }

            if (ss.getSetupType() == SetupType.ASSISTED) {
                ss.getConfig().set("Team." + toRemove[2] + ".Emerald", new ArrayList<>());
                ss.getConfig().set("Team." + toRemove[2] + ".Iron", new ArrayList<>());
                ss.getConfig().set("Team." + toRemove[2] + ".Gold", new ArrayList<>());
                BedWars.nms.sendTitle(p, " ", ss.getTeamColor(toRemove[2]) + toRemove[2] + " generator was removed.", 5, 40, 5);
                Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, p);
                Misc.removeArmorStand(null, nearest, toRemove[1]);
                p.sendMessage(ss.getPrefix() + ss.getTeamColor(toRemove[2]) + toRemove[2] + ChatColor.getLastColors(ss.getPrefix()) + " generators were removed!");
                return true;
            }

            List<String> list = ss.getConfig().getList("Team." + toRemove[2] + "." + toRemove[0]);
            list.remove(toRemove[1]);
            ss.getConfig().set("Team." + toRemove[2] + "." + toRemove[0], list);
            p.sendMessage(ss.getPrefix() + "Removed " + ss.getTeamColor(toRemove[2]) + toRemove[2] + " " + ChatColor.getLastColors(ss.getPrefix()) + toRemove[0] + " generator at location: X:" + nearest.getBlockX() + " Y:" + nearest.getBlockY() + " Z:" + nearest.getZ());
            BedWars.nms.sendTitle(p, " ", ss.getTeamColor(toRemove[2]) + toRemove[2] + " " + ChatColor.GREEN + toRemove[0] + " generator removed.", 5, 40, 5);
            Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, p);
            Misc.removeArmorStand(toRemove[0], nearest, toRemove[1]);
            return true;
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<>();
    }
}
