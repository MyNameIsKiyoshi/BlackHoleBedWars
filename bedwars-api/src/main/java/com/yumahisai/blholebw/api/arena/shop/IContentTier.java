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

package com.yumahisai.blholebw.api.arena.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IContentTier {

    /**
     * Get tier price
     */
    int getPrice();

    /**
     * Get tier currency.
     *
     * {@link Material#AIR} for vault.
     */
    Material getCurrency();


    /**
     * Set tier currency.
     * {@link Material#AIR} for vault.
     */
    void setCurrency(Material currency);

    /**
     * Set tier price.
     */
    void setPrice(int price);

    /**
     * Set tier preview item.
     */
    void setItemStack(ItemStack itemStack);

    /**
     * Set list of items that you receive on buy.
     */
    void setBuyItemsList(List<IBuyItem> buyItemsList);

    /**
     * Get item stack with name and lore in player's language
     */
    ItemStack getItemStack();
    /**
     * Get tier level
     */
    int getValue();

    /**
     * Get items
     */
    List<IBuyItem> getBuyItemsList();
}
