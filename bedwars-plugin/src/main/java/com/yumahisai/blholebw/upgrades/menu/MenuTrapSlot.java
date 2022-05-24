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
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.upgrades.EnemyBaseEnterTrap;
import com.yumahisai.blholebw.api.upgrades.MenuContent;
import com.yumahisai.blholebw.upgrades.UpgradesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuTrapSlot implements MenuContent {

    private ItemStack displayItem;
    private String name;
    private int trap;

    /**
     * @param displayItem display item.
     */
    public MenuTrapSlot(String name, ItemStack displayItem) {
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + name);
        this.name = name;
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_NAME_PATH + name.replace("trap-slot-", ""), "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + name.replace("trap-slot-", ""), Collections.singletonList("&cLore1 not set"));
        Language.saveIfNotExists(Messages.UPGRADES_TRAP_SLOT_ITEM_LORE2_PATH + name.replace("trap-slot-", ""), Collections.singletonList("&cLore2 not set"));
        trap = UpgradesManager.getConfiguration().getInt(name + ".trap");
        if (trap < 0) trap = 0;
        if (trap != 0) trap -= 1;
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        ItemStack i = displayItem.clone();
        EnemyBaseEnterTrap ebe = null;
        if (!team.getActiveTraps().isEmpty()) {
            if (team.getActiveTraps().size() > trap) {
                ebe = team.getActiveTraps().get(trap);
            }
        }
        if (ebe != null){
            i = ebe.getItemStack().clone();
        }
        i.setAmount(trap+1);
        ItemMeta im = i.getItemMeta();
        if (im == null) return i;
        im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_TRAP_SLOT_ITEM_NAME_PATH + name.replace("trap-slot-", ""))
                .replace("{name}", Language.getMsg(player, ebe == null ? Messages.MEANING_NO_TRAP : ebe.getNameMsgPath()))
                .replace("{color}", Language.getMsg(player, ebe == null ? Messages.FORMAT_UPGRADE_COLOR_CANT_AFFORD : Messages.FORMAT_UPGRADE_COLOR_UNLOCKED)));
        List<String> lore = new ArrayList<>();
        if (ebe == null) {
            int cost = UpgradesManager.getConfiguration().getInt(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-start-price");
            if (cost == 0) {
                cost = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-start-price");
            }
            String curr = UpgradesManager.getConfiguration().getString(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-currency");
            if (curr == null) {
                curr = UpgradesManager.getConfiguration().getString("default-upgrades-settings.trap-currency");
            }
            String currency = UpgradesManager.getCurrencyMsg(player, cost, curr);
            if (!team.getActiveTraps().isEmpty()) {
                int multiplier = UpgradesManager.getConfiguration().getInt(team.getArena().getArenaName().toLowerCase() + "-upgrades-settings.trap-increment-price");
                if (multiplier == 0) {
                    multiplier = UpgradesManager.getConfiguration().getInt("default-upgrades-settings.trap-increment-price");
                }
                cost = cost + (team.getActiveTraps().size() * multiplier);
            }
            for (String s : Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + name.replace("trap-slot-", ""))) {
                lore.add(s.replace("{cost}", String.valueOf(cost)).replace("{currency}", currency));
            }
            lore.add("");
            for (String s : Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE2_PATH + name.replace("trap-slot-", ""))) {
                lore.add(s.replace("{cost}", String.valueOf(cost)).replace("{currency}", currency));
            }
        } else {
            lore.addAll(Language.getList(player, ebe.getLoreMsgPath()));
            lore.addAll(Language.getList(player, Messages.UPGRADES_TRAP_SLOT_ITEM_LORE1_PATH + name.replace("trap-slot-", "")));
        }
        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        i.setItemMeta(im);
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
    }

    @Override
    public String getName() {
        return name;
    }
}
