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

package com.yumahisai.blholebw.database;

import com.yumahisai.blholebw.shop.quickbuy.QuickBuyElement;
import com.yumahisai.blholebw.stats.PlayerStats;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface Database {

    /**
     * Initialize database.
     */
    void init();

    /**
     * Check if player has remote stats.
     */
    boolean hasStats(UUID uuid);

    /**
     * Create or replace stats for a player.
     */
    void saveStats(PlayerStats stats);

    PlayerStats fetchStats(UUID uuid);

    /**
     * Set quick buy slot value.
     */
    @Deprecated
    @SuppressWarnings("unused")
    void setQuickBuySlot(UUID uuid, String shopPath, int slot);

    /**
     * Get quick buy slot value.
     */
    String getQuickBuySlots(UUID uuid, int slot);

    /**
     * Get quick buy.
     * slot - identifier string
     */
    HashMap<Integer, String> getQuickBuySlots(UUID uuid, int[] slot);

    /**
     * Check if has quick buy.
     */
    boolean hasQuickBuy(UUID player);

    /**
     * Get a stats value for the given player.
     */
    @SuppressWarnings("unused")
    int getColumn(UUID player, String column);

    /**
     * Get a player level and xp.
     * <p>
     * args 0 is level.
     * args 1 is xp.
     * args 2 is display name.
     * args 3 next level cost.
     */
    Object[] getLevelData(UUID player);

    /**
     * Set a player level data.
     */
    void setLevelData(UUID player, int level, int xp, String displayName, int nextCost);

    /**
     * Set a player language.
     */
    void setLanguage(UUID player, String iso);

    /**
     * Get a player language.
     */
    String getLanguage(UUID player);

    /**
     * @param updateSlots key is slot id and value is the element.
     */
    void pushQuickBuyChanges(HashMap<Integer, String> updateSlots, UUID uuid, List<QuickBuyElement> elementList);
}
