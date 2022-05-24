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

package com.yumahisai.blholebw.listeners.chat;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.commands.shout.ShoutCommand;
import com.yumahisai.blholebw.configuration.Permissions;
import com.yumahisai.blholebw.support.papi.SupportPAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;
import static com.yumahisai.blholebw.api.language.Language.getPlayerLanguage;

public class ChatFormatting implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        if (e == null) return;
        Player p = e.getPlayer();

        // in shared mode we don't want messages from outside the arena to be seen in game
        if (BedWars.getServerType() == ServerType.SHARED && Arena.getArenaByPlayer(p) == null) {
            e.getRecipients().removeIf(pl -> Arena.getArenaByPlayer(pl) != null);
            return;
        }

        // handle chat color. we would need to work on permission inheritance
        if (Permissions.hasPermission(p, Permissions.PERMISSION_CHAT_COLOR, Permissions.PERMISSION_VIP, Permissions.PERMISSION_ALL)) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }

        Language language = getPlayerLanguage(p);

        // handle lobby world for multi arena
        if (BedWars.getServerType() == ServerType.MULTIARENA && p.getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            setRecipients(e, p.getWorld().getPlayers());
        }

        // handle arena chat
        if (Arena.getArenaByPlayer(p) != null) {
            IArena a = Arena.getArenaByPlayer(p);

            // spectator chat
            if (a.isSpectator(p)) {
                setRecipients(e, a.getSpectators());
                e.setFormat(parsePHolders(language.m(Messages.FORMATTING_CHAT_SPECTATOR), p, null));
                return;
            }

            // arena lobby chat
            if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting) {
                setRecipients(e, a.getPlayers());
                e.setFormat(parsePHolders(language.m(Messages.FORMATTING_CHAT_WAITING), p, null));
                return;
            }

            ITeam team = a.getTeam(p);
            String msg = e.getMessage();

            // shout format
            if (isShouting(msg, language)) {
                if (!(p.hasPermission(Permissions.PERMISSION_SHOUT_COMMAND) || p.hasPermission(Permissions.PERMISSION_ALL))) {
                    e.setCancelled(true);
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
                    return;
                }
                if (ShoutCommand.isShoutCooldown(p)) {
                    e.setCancelled(true);
                    p.sendMessage(language.m(Messages.COMMAND_COOLDOWN)
                            .replace("{seconds}", String.valueOf(Math.round(ShoutCommand.getShoutCooldown(p))))
                    );
                    return;
                }
                ShoutCommand.updateShout(p);
                setRecipients(e, a.getPlayers(), a.getSpectators());
                msg = clearShout(msg, language);
                if (msg.isEmpty()) {
                    e.setCancelled(true);
                    return;
                }
                e.setMessage(msg);
                e.setFormat(parsePHolders(language.m(Messages.FORMATTING_CHAT_SHOUT), p, team));
                return;
            }

            // player team chat
            if (a.getMaxInTeam() == 1) {
                setRecipients(e, a.getPlayers(), a.getSpectators());
            } else {
                setRecipients(e, team.getMembers());
            }
            e.setFormat(parsePHolders(language.m(Messages.FORMATTING_CHAT_TEAM), p, team));
            return;
        }

        // multi arena lobby chat
        e.setFormat(parsePHolders(language.m(Messages.FORMATTING_CHAT_LOBBY), p, null));
    }

    private static String parsePHolders(String content, Player player, @Nullable ITeam team) {
        content = content
                .replace("{vPrefix}", BedWars.getChatSupport().getPrefix(player))
                .replace("{vSuffix}", BedWars.getChatSupport().getSuffix(player))
                .replace("{playername}", player.getName())
                .replace("{level}", BedWars.getLevelSupport().getLevel(player))
                .replace("{player}", player.getDisplayName());
        if (team != null) {
            String teamFormat = getMsg(player, Messages.FORMAT_PAPI_PLAYER_TEAM_TEAM)
                    .replace("{TeamColor}", team.getColor().chat() + "")
                    .replace("{TeamName}", team.getDisplayName(Language.getPlayerLanguage(player)).toUpperCase());
            content = content.replace("{team}", teamFormat);
        }
        return SupportPAPI.getSupportPAPI().replace(player, content).replace("{message}", "%2$s");
    }

    private static boolean isShouting(String msg, Language lang) {
        return msg.startsWith("!") || msg.startsWith("shout") ||
                msg.startsWith("SHOUT") || msg.startsWith(lang.m(Messages.MEANING_SHOUT));
    }

    private static String clearShout(String msg, Language lang) {
        if (msg.startsWith("!")) msg = msg.replaceFirst("!", "");
        if (msg.startsWith("SHOUT")) msg = msg.replaceFirst("SHOUT", "");
        if (msg.startsWith("shout")) msg = msg.replaceFirst("shout", "");
        if (msg.startsWith(lang.m(Messages.MEANING_SHOUT))) {
            msg = msg.replaceFirst(lang.m(Messages.MEANING_SHOUT), "");
        }
        return msg.trim();
    }

    @SafeVarargs
    public static void setRecipients(AsyncPlayerChatEvent event, List<Player>... target) {
        if (!BedWars.config.getBoolean(ConfigPath.GENERAL_CHAT_GLOBAL)) {
            event.getRecipients().clear();
            for (List<Player> list : target) {
                event.getRecipients().addAll(list);
            }
        }
    }
}
