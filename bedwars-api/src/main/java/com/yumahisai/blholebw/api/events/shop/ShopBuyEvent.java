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

package com.yumahisai.blholebw.api.events.shop;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.shop.ICategoryContent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopBuyEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player buyer;
    private final IArena arena;
    private final ICategoryContent categoryContent;
    private boolean cancelled = false;

    /**
     * Triggered when a player buys from the shop
     * 
     * @deprecated Use {@link #ShopBuyEvent(Player, IArena, ICategoryContent)}
     */
    @Deprecated
    public ShopBuyEvent(Player buyer, ICategoryContent categoryContent) {
        this.categoryContent = categoryContent;
        this.buyer = buyer;
        this.arena = null;
    }

    /**
     * Triggered when a player buys from the shop
     */
    public ShopBuyEvent(Player buyer, IArena arena, ICategoryContent categoryContent) {
        this.categoryContent = categoryContent;
        this.buyer = buyer;
        this.arena = arena;
    }

    public IArena getArena() {
        return arena;
    }

    /**
     * Get the buyer
     */
    public Player getBuyer() {
        return buyer;
    }

    /**
     * Get the shop category content bought.
     */
    public ICategoryContent getCategoryContent() {
        return categoryContent;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
