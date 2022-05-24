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

package com.yumahisai.blholebw.shop.main;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.events.shop.ShopOpenEvent;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.shop.ShopCache;
import com.yumahisai.blholebw.shop.quickbuy.PlayerQuickBuyCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class ShopIndex {

    private int invSize = 54;
    private String namePath, separatorNamePath, separatorLorePath;
    private List<ShopCategory> categoryList = new ArrayList<>();
    private QuickBuyButton quickBuyButton;
    public ItemStack separatorSelected, separatorStandard;

    public static List<UUID> indexViewers = new ArrayList<>();


    /**
     * Create a shop index
     *
     * @param namePath          Message path for the shop inventory name
     * @param quickBuyButton    Player quick buy preferences cache
     * @param separatorNamePath Message path for the shop separator item name
     * @param separatorLorePath Message path for the shop separator lore name
     * @param separatorSelected ItemStack for selected category indicator
     * @param separatorStandard ItemStack for standard separator
     */
    public ShopIndex(String namePath, QuickBuyButton quickBuyButton, String separatorNamePath, String separatorLorePath, ItemStack separatorSelected, ItemStack separatorStandard) {
        this.namePath = namePath;
        this.separatorLorePath = separatorLorePath;
        this.separatorNamePath = separatorNamePath;
        this.quickBuyButton = quickBuyButton;
        this.separatorStandard = separatorStandard;
        this.separatorSelected = separatorSelected;
    }

    /**
     * Open this shop to a player
     *
     * @param callEvent     true if you want to call the shop open event
     * @param quickBuyCache the player cache regarding his preferences
     * @param player        target player
     */
    public void open(Player player, PlayerQuickBuyCache quickBuyCache, boolean callEvent) {

        if (quickBuyCache == null) return;

        if (callEvent) {
            ShopOpenEvent event = new ShopOpenEvent(player, Arena.getArenaByPlayer(player));
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
        }

        Inventory inv = Bukkit.createInventory(null, invSize, Language.getMsg(player, getNamePath()));

        inv.setItem(getQuickBuyButton().getSlot(), getQuickBuyButton().getItemStack(player));

        for (ShopCategory sc : getCategoryList()) {
            inv.setItem(sc.getSlot(), sc.getItemStack(player));
        }

        addSeparator(player, inv);

        inv.setItem(getQuickBuyButton().getSlot() + 9, getSelectedItem(player));
        //noinspection ConstantConditions
        ShopCache.getShopCache(player.getUniqueId()).setSelectedCategory(getQuickBuyButton().getSlot());

        quickBuyCache.addInInventory(inv, ShopCache.getShopCache(player.getUniqueId()));

        player.openInventory(inv);
        if (!indexViewers.contains(player.getUniqueId())) {
            indexViewers.add(player.getUniqueId());
        }
    }


    /**
     * Add shop separator between categories and items
     */
    public void addSeparator(Player player, Inventory inv) {
        ItemStack i = separatorStandard.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, separatorNamePath));
            im.setLore(Language.getList(player, separatorLorePath));
            i.setItemMeta(im);
        }

        for (int x = 9; x < 18; x++) {
            inv.setItem(x, i);
        }
    }

    /**
     * This is the item that indicates the selected category
     */
    public ItemStack getSelectedItem(Player player) {
        ItemStack i = separatorSelected.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, separatorNamePath));
            im.setLore(Language.getList(player, separatorLorePath));
            i.setItemMeta(im);
        }
        return i;
    }

    /**
     * Add a shop category
     */
    public void addShopCategory(ShopCategory sc) {
        categoryList.add(sc);
        BedWars.debug("Adding shop category: " + sc + " at slot " + sc.getSlot());
    }

    /**
     * Get the inventory name path
     */
    public String getNamePath() {
        return namePath;
    }

    /**
     * Get the inventory size
     */
    public int getInvSize() {
        return invSize;
    }

    /**
     * Get the shop's categories
     */
    public List<ShopCategory> getCategoryList() {
        return categoryList;
    }

    /**
     * Get the quick buy button
     */
    public QuickBuyButton getQuickBuyButton() {
        return quickBuyButton;
    }

    public static List<UUID> getIndexViewers() {
        return new ArrayList<>(indexViewers);
    }
}
