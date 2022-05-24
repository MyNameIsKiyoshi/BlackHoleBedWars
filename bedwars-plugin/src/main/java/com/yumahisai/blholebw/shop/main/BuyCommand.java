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

package com.yumahisai.blholebw.shop.main;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.shop.IBuyItem;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BuyCommand implements IBuyItem {

    private final List<String> asPlayer = new ArrayList<>();
    private final List<String> asConsole = new ArrayList<>();
    private final String upgradeIdentifier;


    public BuyCommand(String path, YamlConfiguration yml, String upgradeIdentifier) {
        BedWars.debug("Loading BuyCommand: " + path);
        this.upgradeIdentifier = upgradeIdentifier;
        for (String cmd : yml.getStringList(path + ".as-console")) {
            if (cmd.startsWith("/")) {
                cmd = cmd.replaceFirst("/", "");
            }
            asConsole.add(cmd);
        }
        for (String cmd : yml.getStringList(path + ".as-player")) {
            if (!cmd.startsWith("/")) {
                cmd = "/" + cmd;
            }
            asPlayer.add(cmd);
        }
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void give(Player player, IArena arena) {
        BedWars.debug("Giving BuyCMD: " + getUpgradeIdentifier() + " to: " + player.getName());
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        ITeam team = arena.getTeam(player);
        String teamName = team == null ? "null" : team.getName();
        String teamDisplay = team == null ? "null" : team.getDisplayName(Language.getPlayerLanguage(player));
        String teamColor = team == null ? ChatColor.WHITE.toString() : team.getColor().chat().toString();
        String arenaIdentifier = arena.getArenaName();
        String arenaWorld = arena.getWorldName();
        String arenaDisplay = arena.getDisplayName();
        String arenaGroup = arena.getGroup();
        for (String playerCmd : asPlayer) {
            player.chat(playerCmd.replace("{player}", playerName)
                    .replace("{player_uuid}", playerUUID)
                    .replace("{team}", teamName).replace("{team_display}", teamDisplay)
                    .replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier)
                    .replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay)
                    .replace("{arena_group}", arenaGroup));
        }
        for (String consoleCmd : asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCmd
                    .replace("{player}", playerName).replace("{player_uuid}", playerUUID)
                    .replace("{team}", teamName).replace("{team_display}", teamDisplay)
                    .replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier)
                    .replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay)
                    .replace("{arena_group}", arenaGroup));
        }
    }

    @Override
    public String getUpgradeIdentifier() {
        return upgradeIdentifier;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {

    }

    @Override
    public boolean isAutoEquip() {
        return false;
    }

    @Override
    public void setAutoEquip(boolean autoEquip) {

    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean permanent) {

    }
}
