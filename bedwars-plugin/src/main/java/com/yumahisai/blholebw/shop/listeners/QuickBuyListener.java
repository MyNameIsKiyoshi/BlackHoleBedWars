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

package com.yumahisai.blholebw.shop.listeners;

import com.yumahisai.blholebw.api.events.player.PlayerJoinArenaEvent;
import com.yumahisai.blholebw.api.events.player.PlayerReJoinEvent;
import com.yumahisai.blholebw.shop.quickbuy.PlayerQuickBuyCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuickBuyListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArenaJoin(PlayerJoinArenaEvent e){
        if (e == null) return;
        if (e.isSpectator()) return;
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(e.getPlayer().getUniqueId());
        if (cache != null) {
            cache.destroy();
        }
        new PlayerQuickBuyCache(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArenaJoin(PlayerReJoinEvent e){
        if (e == null) return;
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(e.getPlayer().getUniqueId());
        if (cache != null) {
            cache.destroy();
        }
        new PlayerQuickBuyCache(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e){
        if (e == null) return;
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(e.getPlayer().getUniqueId());
        if (cache == null) return;
        cache.destroy();
    }
}
