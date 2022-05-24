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

package com.yumahisai.blholebw.upgrades.menu;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.upgrades.EnemyBaseEnterTrap;
import com.yumahisai.blholebw.api.upgrades.MenuContent;
import com.yumahisai.blholebw.api.upgrades.TeamUpgrade;
import com.yumahisai.blholebw.api.upgrades.UpgradesIndex;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.upgrades.UpgradesManager;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InternalMenu implements UpgradesIndex {

    private String name;
    private HashMap<Integer, MenuContent> menuContentBySlot = new HashMap<>();

    /**
     * Create an upgrade menu for an arena group.
     *
     * @param groupName arena group name.
     */
    public InternalMenu(String groupName) {
        this.name = groupName.toLowerCase();
        Language.saveIfNotExists(Messages.UPGRADES_MENU_GUI_NAME_PATH + groupName.toLowerCase(), "&8Upgrades & Traps");
    }

    @Override
    public void open(Player player) {
        IArena a = Arena.getArenaByPlayer(player);
        if (a == null) return;
        if (!a.isPlayer(player)) return;
        ITeam team = a.getTeam(player);
        if (team == null) return;
        if (!BedWars.getAPI().getArenaUtil().isPlaying(player)) return;
        Inventory inv = Bukkit.createInventory(null, 45, Language.getMsg(player, Messages.UPGRADES_MENU_GUI_NAME_PATH + name));
        for (Map.Entry<Integer, MenuContent> entry : menuContentBySlot.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getDisplayItem(player, team));
        }
        player.openInventory(inv);
        UpgradesManager.setWatchingUpgrades(player.getUniqueId());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean addContent(MenuContent content, int slot) {
        if (menuContentBySlot.get(slot) != null) return false;
        menuContentBySlot.put(slot, content);
        return true;
    }

    public int countTiers() {
        int count = 0;
        for (MenuContent content : menuContentBySlot.values()){
            if (content instanceof TeamUpgrade && !(content instanceof EnemyBaseEnterTrap)){
                TeamUpgrade tu = (TeamUpgrade) content;
                count+=tu.getTierCount();
            }
        }
        return count;
    }

    @Override
    public ImmutableMap<Integer, MenuContent> getMenuContentBySlot() {
        return ImmutableMap.copyOf(menuContentBySlot);
    }
}
