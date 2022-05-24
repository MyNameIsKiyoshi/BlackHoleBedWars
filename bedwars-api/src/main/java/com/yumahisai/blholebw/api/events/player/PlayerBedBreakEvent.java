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

package com.yumahisai.blholebw.api.events.player;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.function.Function;

public class PlayerBedBreakEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final IArena arena;
    private final ITeam playerTeam;
    private final ITeam victimTeam;
    private Function<Player, String> message;
    private Function<Player, String> title;
    private Function<Player, String> subTitle;

    /**
     * Called when a bed gets destroyed.
     */
    public PlayerBedBreakEvent(Player p, ITeam playerTeam, ITeam victimTeam, IArena arena, Function<Player, String> message, Function<Player, String> title, Function<Player, String> subTitle) {
        this.player = p;
        this.playerTeam = playerTeam;
        this.victimTeam = victimTeam;
        this.arena = arena;
        this.message = message;
        this.title = title;
        this.subTitle = subTitle;
    }

    /**
     * Get the player team.
     */
    public ITeam getPlayerTeam() {
        return playerTeam;
    }

    /**
     * Get the team who got the bed destroyed.
     */
    public ITeam getVictimTeam() {
        return victimTeam;
    }

    public IArena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Get chat message.
     */
    public Function<Player, String> getMessage() {
        return message;
    }

    /**
     * Set chat message.
     */
    public void setMessage(Function<Player, String> message) {
        this.message = message;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Function<Player, String> getTitle() {
        return title;
    }

    public void setTitle(Function<Player, String> title) {
        this.title = title;
    }

    public Function<Player, String> getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(Function<Player, String> subTitle) {
        this.subTitle = subTitle;
    }
}
