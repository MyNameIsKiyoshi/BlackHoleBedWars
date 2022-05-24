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

package com.yumahisai.blholebw.shop.quickbuy;

import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.shop.ShopCache;
import com.yumahisai.blholebw.shop.ShopManager;
import com.yumahisai.blholebw.shop.main.CategoryContent;
import com.yumahisai.blholebw.shop.main.ShopCategory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class QuickBuyAdd {

    public static HashMap<UUID, CategoryContent> quickBuyAdds = new HashMap<>();

    public QuickBuyAdd(Player player, CategoryContent cc){
        ShopCategory.categoryViewers.remove(player.getUniqueId());
        open(player, cc);
    }

    public void open(Player player, CategoryContent cc){
        Inventory inv = Bukkit.createInventory(null, ShopManager.getShop().getInvSize(), Language.getMsg(player, Messages.SHOP_QUICK_ADD_NAME));
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId());
        ShopCache sc = ShopCache.getShopCache(player.getUniqueId());
        if (sc == null || cache == null){
            player.closeInventory();
        }
        inv.setItem(4, cc.getItemStack(player, Objects.requireNonNull(sc)));

        Objects.requireNonNull(cache).addInInventory(inv, sc);

        player.openInventory(inv);
        quickBuyAdds.put(player.getUniqueId(), cc);
    }

    public static HashMap<UUID, CategoryContent> getQuickBuyAdds() {
        return new HashMap<>(quickBuyAdds);
    }
}
