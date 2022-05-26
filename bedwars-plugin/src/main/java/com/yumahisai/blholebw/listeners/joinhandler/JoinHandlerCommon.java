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

package com.yumahisai.blholebw.listeners.joinhandler;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinHandlerCommon implements Listener {

    // Used to show some details to YumaHisai
    // No sensitive data
    protected static void displayCustomerDetails(Player player) {
        if (player == null) return;
        //TODO IMPROVE, ADD MORE DETAILS
        if (player.getName().equalsIgnoreCase("YumaHisai")) {
            player.sendMessage("§d§lBlackHoleBedWars By YumaHisai");
            player.sendMessage("§dUser ID: §5" + player.getUniqueId());
            player.sendMessage("§dAuthentication: §5Creator");
            player.sendMessage("§d§lBlackHoleBedWars By YumaHisai");
        }
    }

    @EventHandler
    public void requestLanguage(AsyncPlayerPreLoginEvent e) {
        String iso = BedWars.getRemoteDatabase().getLanguage(e.getUniqueId());
        Bukkit.getScheduler().runTask(BedWars.plugin, () -> Language.setPlayerLanguage(e.getUniqueId(), iso));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removeLanguage(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            Language.setPlayerLanguage(e.getPlayer().getUniqueId(), null);
        }
    }
}
