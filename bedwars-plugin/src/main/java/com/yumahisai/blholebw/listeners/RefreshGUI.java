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

import com.yumahisai.blholebw.api.events.gameplay.GameStateChangeEvent;
import com.yumahisai.blholebw.api.events.player.PlayerJoinArenaEvent;
import com.yumahisai.blholebw.api.events.player.PlayerLeaveArenaEvent;
import com.yumahisai.blholebw.api.events.server.ArenaDisableEvent;
import com.yumahisai.blholebw.api.events.server.ArenaEnableEvent;
import com.yumahisai.blholebw.arena.ArenaGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RefreshGUI implements Listener {

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent e){
        if (e == null) return;
        int size = e.getArena().getPlayers().size();
        for (Player p : Bukkit.getOnlinePlayers()){
            ArenaGUI.refreshInv(p, e.getArena(), size);
        }
    }

    @EventHandler
    public void onPlayerJoinArena(PlayerJoinArenaEvent e){
        if (e == null) return;
        int size = e.getArena().getPlayers().size();
        if (!e.isSpectator()){
            size++;
        }
        for (Player p : Bukkit.getOnlinePlayers()){
            ArenaGUI.refreshInv(p, e.getArena(), size);
        }
    }

    @EventHandler
    public void onPlayerLeaveArena(PlayerLeaveArenaEvent e){
        if (e == null) return;
        int size = e.getArena().getPlayers().size();
        if (!e.isSpectator()){
            size--;
        }
        for (Player p : Bukkit.getOnlinePlayers()){
            ArenaGUI.refreshInv(p, e.getArena(), size);
        }
    }

    @EventHandler
    public void onArenaEnable(ArenaEnableEvent e){
        if (e == null) return;
        for (Player p : Bukkit.getOnlinePlayers()){
            ArenaGUI.refreshInv(p, e.getArena(), 0);
        }
    }

    @EventHandler
    public void onArenaDisable(ArenaDisableEvent e){
        for (Player p : Bukkit.getOnlinePlayers()){
            ArenaGUI.refreshInv(p, null, 0);
        }
    }
}
