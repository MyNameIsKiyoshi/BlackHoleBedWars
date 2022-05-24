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

package com.yumahisai.blholebw.listeners.arenaselector;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.ArenaGUI;
import com.yumahisai.blholebw.configuration.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ArenaSelectorListener implements Listener {

    public static final String ARENA_SELECTOR_GUI_IDENTIFIER = "arena=";

    @EventHandler
    public void onArenaSelectorClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ArenaGUI.ArenaSelectorHolder)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.getType() == Material.AIR) return;
        if (!BedWars.nms.isCustomBedWarsItem(item)) return;

        String data = BedWars.nms.getCustomData(item);
        if (data.startsWith("RUNCOMMAND")) {
            Bukkit.dispatchCommand(player, data.split("_")[1]);
        }

        if (!data.contains(ARENA_SELECTOR_GUI_IDENTIFIER)) return;

        String arenaName = data.split("=")[1];
        IArena arena = Arena.getArenaByName(arenaName);
        if (arena == null) return;

        if (event.getClick() == ClickType.LEFT) {
            if ((arena.getStatus() == GameState.waiting || arena.getStatus() == GameState.starting) && arena.addPlayer(player, false)) {
                Sounds.playSound("join-allowed", player);
            } else {
                Sounds.playSound("join-denied", player);
                player.sendMessage(Language.getMsg(player, Messages.ARENA_JOIN_DENIED_SELECTOR));
            }
        } else if (event.getClick() == ClickType.RIGHT) {
            if (arena.getStatus() == GameState.playing && arena.addSpectator(player, false, null)) {
                Sounds.playSound("spectate-allowed", player);
            } else {
                player.sendMessage(Language.getMsg(player, Messages.ARENA_SPECTATE_DENIED_SELECTOR));
                Sounds.playSound("spectate-denied", player);
            }
        }

        player.closeInventory();
    }
}
