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

package com.yumahisai.blholebw.upgrades.upgradeaction;

import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.upgrades.UpgradeAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class DispatchCommand implements UpgradeAction {

    public enum CommandType {
        ONCE_AS_CONSOLE, FOREACH_MEMBER_AS_CONSOLE, FOREACH_MEMBER_AS_PLAYER;

        private void dispatch(ITeam team, String command) {
            switch (this) {
                case ONCE_AS_CONSOLE:
                    if (command.startsWith("/")) {
                        command = command.replaceFirst("/", "");
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    break;
                case FOREACH_MEMBER_AS_CONSOLE:
                    if (command.startsWith("/")) {
                        command = command.replaceFirst("/", "");
                    }
                    for (Player player : team.getMembers()) {
                        String playerName = player.getName();
                        String playerUUID = player.getUniqueId().toString();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                .replace("{player}", playerName).replace("{player_uuid}", playerUUID));
                    }
                    break;
                case FOREACH_MEMBER_AS_PLAYER:
                    if (!command.startsWith("/")) {
                        command = "/" + command;
                    }
                    for (Player player : team.getMembers()) {
                        String playerName = player.getName();
                        String playerUUID = player.getUniqueId().toString();
                        player.chat(command.replace("{player}", playerName).replace("{player_uuid}", playerUUID));
                    }
                    break;
            }
        }
    }

    private final CommandType commandType;

    private final String command;

    public DispatchCommand(CommandType commandType, String command) {
        this.commandType = commandType;
        this.command = command;
    }


    @Override
    public void onBuy(@Nullable Player player, ITeam team) {
        String buyerName = player == null ? "null" : player.getName();
        String buyerUUID = player == null ? "null" : player.getUniqueId().toString();
        String teamName = team.getName();
        String teamDisplay = team.getDisplayName(Language.getDefaultLanguage());
        String teamColor = team.getColor().chat().toString();
        String arenaIdentifier = team.getArena().getArenaName();
        String arenaWorld = team.getArena().getWorldName();
        String arenaDisplay = team.getArena().getDisplayName();
        String arenaGroup = team.getArena().getGroup();
        commandType.dispatch(team, command
                .replace("{buyer}", buyerName)
                .replace("{buyer_uuid}", buyerUUID)
                .replace("{team}", teamName).replace("{team_display}", teamDisplay)
                .replace("{team_color}", teamColor).replace("{arena}", arenaIdentifier)
                .replace("{arena_world}", arenaWorld).replace("{arena_display}", arenaDisplay)
                .replace("{arena_group}", arenaGroup));
    }
}
