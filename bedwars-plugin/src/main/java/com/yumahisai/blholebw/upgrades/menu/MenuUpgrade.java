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
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.events.upgrades.UpgradeBuyEvent;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.upgrades.MenuContent;
import com.yumahisai.blholebw.api.upgrades.TeamUpgrade;
import com.yumahisai.blholebw.api.upgrades.UpgradeAction;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.configuration.Sounds;
import com.yumahisai.blholebw.upgrades.UpgradesManager;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuUpgrade implements MenuContent, TeamUpgrade {

    private String name;
    private List<UpgradeTier> tiers = new LinkedList<>();

    /**
     * Create a new upgrade element.
     *
     * @param name identifier.
     */
    public MenuUpgrade(String name) {
        this.name = name;
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        if (tiers.isEmpty()) return new ItemStack(Material.BEDROCK);

        int tier = -1;
        if (team.getTeamUpgradeTiers().containsKey(getName())) {
            tier = team.getTeamUpgradeTiers().get(getName());
        }

        boolean highest = getTiers().size() == tier + 1 && team.getTeamUpgradeTiers().containsKey(getName());
        if (!highest) tier += 1;
        UpgradeTier ut = getTiers().get(tier);
        boolean afford = UpgradesManager.getMoney(player, ut.getCurrency()) >= ut.getCost();

        ItemStack i = new ItemStack(tiers.get(tier).getDisplayItem());
        ItemMeta im = i.getItemMeta();
        if (im == null) return i;
        String color;
        if (!highest){
            if (afford){
                color = Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CAN_AFFORD);
            } else {
                color = Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_CANT_AFFORD);
            }
        } else {
            color = Language.getMsg(player, Messages.FORMAT_UPGRADE_COLOR_UNLOCKED);
        }

        im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_UPGRADE_TIER_ITEM_NAME.replace("{name}", this.getName().replace("upgrade-", "")).replace("{tier}", ut.getName())).replace("{color}", color));

        List<String> lore = new ArrayList<>();
        String currencyMsg = UpgradesManager.getCurrencyMsg(player, ut);
        for (String s : Language.getList(player, Messages.UPGRADES_UPGRADE_TIER_ITEM_LORE.replace("{name}", this.getName().replace("upgrade-", "")).replace("{tier}", ut.getName()))){
            lore.add(s.replace("{cost}", String.valueOf(ut.getCost())).replace("{currency}", currencyMsg).replace("{tierColor}",
                    Language.getMsg(player, highest ? Messages.FORMAT_UPGRADE_TIER_UNLOCKED : Messages.FORMAT_UPGRADE_TIER_LOCKED)).replace("{color}", color));
        }
        if (highest){
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_UNLOCKED).replace("{color}", color));
        } else if (afford){
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_CLICK_TO_BUY).replace("{color}", color));
        } else {
            lore.add(Language.getMsg(player, Messages.UPGRADES_LORE_REPLACEMENT_INSUFFICIENT_MONEY).replace("{currency}", currencyMsg).replace("{color}", color));
        }
        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        i.setItemMeta(im);
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
        int tier = -1;
        if (team.getTeamUpgradeTiers().containsKey(getName())) {
            tier = team.getTeamUpgradeTiers().get(getName());
        }
        UpgradeTier ut;
        if (getTiers().size() - 1 > tier) {
            ut = getTiers().get(tier + 1);

            int money = UpgradesManager.getMoney(player, ut.getCurrency());
            if (money < ut.getCost()) {
                Sounds.playSound(ConfigPath.SOUNDS_INSUFF_MONEY, player);
                player.sendMessage(Language.getMsg(player, Messages.SHOP_INSUFFICIENT_MONEY)
                        .replace("{currency}", UpgradesManager.getCurrencyMsg(player, ut))
                        .replace("{amount}", String.valueOf(ut.getCost() - money)));
                player.closeInventory();
                return;
            }

            final UpgradeBuyEvent event;
            Bukkit.getPluginManager().callEvent(event = new UpgradeBuyEvent(this, player, team));
            if(event.isCancelled()) return;
            
            if (ut.getCurrency() == Material.AIR) {
                BedWars.getEconomy().buyAction(player, ut.getCost());
            } else {
                BedWars.getAPI().getShopUtil().takeMoney(player, ut.getCurrency(), ut.getCost());
            }

            if (team.getTeamUpgradeTiers().containsKey(getName())) {
                team.getTeamUpgradeTiers().replace(getName(), team.getTeamUpgradeTiers().get(getName()) + 1);
            } else {
                team.getTeamUpgradeTiers().put(getName(), 0);
            }
            Sounds.playSound(ConfigPath.SOUNDS_BOUGHT, player);
            for (UpgradeAction a : ut.getUpgradeActions()) {
                a.onBuy(player, team);
            }

            for (Player p1 : team.getMembers()) {
                p1.sendMessage(Language.getMsg(p1, Messages.UPGRADES_UPGRADE_BOUGHT_CHAT).replace("{playername}", player.getName()).replace("{player}", player.getDisplayName()).replace("{upgradeName}",
                        ChatColor.stripColor(Language.getMsg(p1, Messages.UPGRADES_UPGRADE_TIER_ITEM_NAME.replace("{name}", getName()
                                .replace("upgrade-", "")).replace("{tier}", ut.getName())))).replace("{color}", ""));
            }

            ImmutableMap<Integer, MenuContent> menuContentBySlot = UpgradesManager.getMenuForArena(Arena.getArenaByPlayer(player)).getMenuContentBySlot();
            Inventory inv = player.getOpenInventory().getTopInventory();
            for (Map.Entry<Integer, MenuContent> entry : menuContentBySlot.entrySet()) {
                inv.setItem(entry.getKey(), entry.getValue().getDisplayItem(player, team));
            }

        }
    }

    /**
     * Load a upgrade element tiers.
     *
     * @param upgradeTier tier.
     * @return false if something went wrong.
     */
    public boolean addTier(UpgradeTier upgradeTier) {
        for (UpgradeTier ut : tiers) {
            if (ut.getName().equalsIgnoreCase(upgradeTier.getName())) return false;
        }
        tiers.add(upgradeTier);
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getTierCount() {
        return tiers.size();
    }

    /**
     * @return tiers list.
     */
    public List<UpgradeTier> getTiers() {
        return Collections.unmodifiableList(tiers);
    }
}
