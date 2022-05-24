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

package com.yumahisai.blholebw.halloween.shop;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.shop.IBuyItem;
import com.yumahisai.blholebw.api.arena.shop.IContentTier;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.shop.ShopCache;
import com.yumahisai.blholebw.shop.main.CategoryContent;
import com.yumahisai.blholebw.shop.main.ShopCategory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class PumpkinContent extends CategoryContent {

    private final int slot;

    public PumpkinContent(ShopCategory father) {
        super(null, null, null, null, father);

        int foundSlot = -1;
        for (int i = 19; i < 26; i++){
            int finalI = i;
            if (father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)){
                foundSlot = i;
                break;
            }
        }
        if (foundSlot == -1) {
            for (int i = 28; i < 35; i++) {
                int finalI = i;
                if (father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)) {
                    foundSlot = i;
                    break;
                }
            }
        }
        if (foundSlot == -1) {
            for (int i = 37; i < 44; i++) {
                int finalI = i;
                if (father.getCategoryContentList().stream().noneMatch(categoryContent -> categoryContent.getSlot() == finalI)) {
                    foundSlot = i;
                    break;
                }
            }
        }

        this.slot = foundSlot;
        setLoaded(slot != -1);
        if (!isLoaded()) return;
        OneTier pumpkinTier = new OneTier();
        getContentTiers().add(pumpkinTier);
    }

    @Override
    public String getIdentifier() {
        return "halloween-special-pumpkin";
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }


    @Override
    public ItemStack getItemStack(Player player) {
        IContentTier tier = getContentTiers().get(0);
        ItemStack pumpkin = tier.getItemStack();

        boolean canAfford = calculateMoney(player, tier.getCurrency()) >= tier.getPrice();
        String translatedCurrency = getMsg(player, getCurrencyMsgPath(tier));

        String buyStatus;

        if (!canAfford) {
            buyStatus = getMsg(player, Messages.SHOP_LORE_STATUS_CANT_AFFORD).replace("{currency}", translatedCurrency);
        } else {
            buyStatus = getMsg(player, Messages.SHOP_LORE_STATUS_CAN_BUY);
        }
        ChatColor cColor = getCurrencyColor(tier.getCurrency());

        pumpkin.setAmount(12);
        ItemMeta itemMeta = pumpkin.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Happy Halloween!");
        itemMeta.setLore(Arrays.asList("", cColor + String.valueOf(tier.getPrice()) + " " + cColor + translatedCurrency, " ", buyStatus));
        pumpkin.setItemMeta(itemMeta);
        return pumpkin;
    }

    @Override
    public ItemStack getItemStack(Player player, ShopCache shopCache) {
        return getItemStack(player);
    }

    private static class OneTier implements IContentTier {

        @Override
        public int getPrice() {
            return 4;
        }

        @Override
        public Material getCurrency() {
            return Material.IRON_INGOT;
        }

        @Override
        public void setCurrency(Material currency) {

        }

        @Override
        public void setPrice(int price) {

        }

        @Override
        public void setItemStack(ItemStack itemStack) {

        }

        @Override
        public void setBuyItemsList(List<IBuyItem> buyItemsList) {

        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Material.PUMPKIN, 12);
        }

        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public List<IBuyItem> getBuyItemsList() {
            return Collections.singletonList(new FinalItem());
        }
    }

    private static class FinalItem implements IBuyItem {

        @Override
        public boolean isLoaded() {
            return true;
        }

        @Override
        public void give(Player player, IArena arena) {
            player.getInventory().addItem(new ItemStack(Material.PUMPKIN, 12));
        }

        @Override
        public String getUpgradeIdentifier() {
            return null;
        }

        @Override
        public ItemStack getItemStack() {
            return null;
        }

        @Override
        public void setItemStack(ItemStack itemStack) {

        }

        @Override
        public boolean isAutoEquip() {
            return false;
        }

        @Override
        public void setAutoEquip(boolean autoEquip) {

        }

        @Override
        public boolean isPermanent() {
            return false;
        }

        @Override
        public void setPermanent(boolean permanent) {

        }
    }
}
