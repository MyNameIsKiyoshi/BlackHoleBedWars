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

package com.yumahisai.blholebw.listeners;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class CmdProcess implements Listener {

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();

        if (e.getMessage().equals("/party sethome")){
            p.sendMessage(getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            e.setCancelled(true);
        }

        if (e.getMessage().equals("/party home")){
            p.sendMessage(getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            e.setCancelled(true);
        }

        if (p.hasPermission(Permissions.PERMISSION_COMMAND_BYPASS)) return;
        String[] cmd = e.getMessage().replaceFirst("/", "").split(" ");
        if (cmd.length == 0) return;
        if (Arena.isInArena(p)) {
            if (!BedWars.config.getList(ConfigPath.CENERAL_CONFIGURATION_ALLOWED_COMMANDS).contains(cmd[0])) {
                p.sendMessage(getMsg(p, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
                e.setCancelled(true);
            }
        }
    }
}
